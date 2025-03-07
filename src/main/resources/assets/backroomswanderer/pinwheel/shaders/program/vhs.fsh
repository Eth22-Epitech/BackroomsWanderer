#version 150

// === Uniforms ===
uniform sampler2D DiffuseSampler0;
uniform float time;
uniform float resolutionX;
uniform float resolutionY;

// === Calculated Resolution Vector ===
vec2 resolution = vec2(resolutionX, resolutionY);

// === Inputs/Outputs ===
in vec2 texCoord;
out vec4 OutColor;

// === Color Constants ===
const vec4 BLACK = vec4(0.0, 0.0, 0.0, 1.0);
const vec4 WHITE = vec4(1.0, 1.0, 1.0, 1.0);

// === Color Weight for Luminance Calculation (Rec. 709) ===
const vec3 W = vec3(0.2126, 0.7152, 0.0722);

// === Functions to compute luminance ===
float getLuminance(vec3 color) {
    return dot(color, W);
}
float getLuminance(vec4 color) {
    return dot(color.rgb, W);
}

// === Blur Proxy ===
vec4 sampleBlurred(vec2 uv, float bias) {
    return texture(DiffuseSampler0, uv, bias);
}

// === Noise ===
float GoldNoise(vec2 xy, float seed)
{
    return fract(tan(distance(xy * (seed * 1.6180339887498948482), vec2(12.9898,78.233))) * 43758.5453);
}

vec4 Noise(float grainSize, bool monochromatic, vec2 fragCoord, float fps)
{
    float seed = fps > 0.0 ? floor(fract(time) * fps) / fps : time;
    seed += 1.0;

    if (grainSize > 1.0) {
        fragCoord.x = floor(fragCoord.x / grainSize);
        fragCoord.y = floor(fragCoord.y / grainSize);
    }

    fragCoord.x += 1.0;

    float r = GoldNoise(fragCoord, seed);
    float g = monochromatic ? r : GoldNoise(fragCoord, seed + 1.0);
    float b = monochromatic ? r : GoldNoise(fragCoord, seed + 2.0);

    return vec4(r, g, b, 1.0);
}

// === Barrel Distortion ===
vec2 applyBarrelDistortion(vec2 coord) {
    vec2 centered = coord * 2.0 - 1.0;
    float strength = 0.05;
    float dist = dot(centered, centered);
    vec2 distorted = centered * (0.9 + strength * dist);
    distorted = distorted * 0.4 + 0.5;
    return clamp(distorted, 0.0, 1.0);
}

// === Shrinking Functions (lower res luma/chroma bands) ===
vec4 sampleShrink(sampler2D tex, vec2 fragCoord, vec2 resolution, float shrinkRatio, float mipBias) {
    float bandWidth = resolution.x / (resolution.x * shrinkRatio);
    float t = mod(fragCoord.x, bandWidth) / bandWidth;

    // Sample current band
    fragCoord.x = floor(fragCoord.x * shrinkRatio) / shrinkRatio;
    vec2 uv = fragCoord / resolution;
    vec4 colorA = texture(tex, uv, mipBias);

    // Sample next band for interpolation
    uv.x += bandWidth / resolution.x;
    vec4 colorB = texture(tex, uv, mipBias);

    return mix(colorA, colorB, t);
}

// === Blend Helpers ===
vec3 clipColor(vec3 c) {
    float l = getLuminance(c);
    float n = min(min(c.r, c.g), c.b);
    float x = max(max(c.r, c.g), c.b);

    if (n < 0.0) c = l + ((c - l) * l) / (l - n);
    if (x > 1.0) c = l + ((c - l) * (1.0 - l)) / (x - l);

    return c;
}

vec3 setLuminance(vec3 c, float l) {
    return clipColor(c + (l - getLuminance(c)));
}

vec4 blendColor(vec4 base, vec4 blend) {
    vec3 c = setLuminance(blend.rgb, getLuminance(base));
    return vec4(c, blend.a);
}

vec4 blendLuminosity(vec4 base, vec4 blend) {
    vec3 c = setLuminance(base.rgb, getLuminance(blend));
    return vec4(c, blend.a);
}

vec4 BlendSoftLight(vec4 base, vec4 blend, float opacity)
{
    vec3 result = mix(base.rgb, base.rgb * (blend.rgb + base.rgb * (1.0 - (1.0 - blend.rgb) * (1.0 - base.rgb))), opacity);
    return vec4(result, base.a);
}

// === Chromatic Aberration ===
vec4 applyChromaticAberration(sampler2D tex, vec2 uv, vec4 baseColor) {
    vec2 center = vec2(0.5);
    float dist = distance(uv, center);
    float offset = 0.006 * dist;

    vec4 shiftedColor;
    shiftedColor.r = texture(tex, uv + vec2(offset, 0.0)).r;
    shiftedColor.g = baseColor.g;
    shiftedColor.b = texture(tex, uv - vec2(offset, 0.0)).b;
    shiftedColor.a = baseColor.a;
    return shiftedColor;
}

void main() {
    vec2 fragCoord = texCoord * resolution;

    vec2 distortedCoord = applyBarrelDistortion(texCoord);
    vec2 distortedFragCoord = distortedCoord * resolution;

    vec4 luma = sampleShrink(DiffuseSampler0, distortedFragCoord, resolution, 0.5, 0.0);
    luma = blendLuminosity(vec4(0.5), luma);

    vec4 chroma = sampleShrink(DiffuseSampler0, distortedFragCoord, resolution, 1.0 / 32.0, 3.0);
    chroma = blendColor(luma, chroma);

    vec4 finalColor = applyChromaticAberration(DiffuseSampler0, distortedCoord, chroma);

    const float NOISE_BLEND = 0.05;
    const float LINE_HEIGHT = 0.2;
    const float NOISE_GRAIN_SIZE = 8.0;

    bool updateOddLines = mod(float(int(60.0)), 2.0) == 0.0;
    bool isOddLine = mod(floor(fragCoord.y), 2.0 * LINE_HEIGHT) >= LINE_HEIGHT;

    if (isOddLine && updateOddLines || !isOddLine && !updateOddLines) {
        finalColor = texture(DiffuseSampler0, texCoord);
    }

    vec4 noise = Noise(NOISE_GRAIN_SIZE, true, fragCoord, 60.0);

    finalColor = BlendSoftLight(finalColor, noise, NOISE_BLEND);

    OutColor = finalColor;
}
