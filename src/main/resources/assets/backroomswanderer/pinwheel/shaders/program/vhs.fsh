#version 150

uniform sampler2D DiffuseSampler0;
uniform float time;
uniform float resolutionX;
uniform float resolutionY;

vec2 resolution = vec2(resolutionX, resolutionY);

in vec2 texCoord;
out vec4 OutColor;

// === Color Weight for Luminance Calculation (Rec. 709) ===
const vec3 W = vec3(0.2126, 0.7152, 0.0722);

// === Functions to compute luminance ===
float getLuminance(vec3 color) {
    return dot(color, W);
}
float getLuminance(vec4 color) {
    return dot(color.rgb, W);
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
vec4 sampleShrink(vec2 fragCoord, float shrinkRatio, float mipBias) {
    float bandWidth = resolution.x / (resolution.x * shrinkRatio);
    float t = mod(fragCoord.x, bandWidth) / bandWidth;

    // Sample current band
    fragCoord.x = floor(fragCoord.x * shrinkRatio) / shrinkRatio;
    vec2 uv = fragCoord / resolution;
    vec4 colorA = texture(DiffuseSampler0, uv, mipBias);

    // Sample next band for interpolation
    uv.x += bandWidth / resolution.x;
    vec4 colorB = texture(DiffuseSampler0, uv, mipBias);

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

// === Chromatic Aberration ===
vec4 applyChromaticAberration(vec2 uv, vec4 baseColor) {
    vec2 center = vec2(0.5);
    float dist = distance(uv, center);
    float offset = 0.008 * dist;

    vec4 shiftedColor;
    shiftedColor.r = texture(DiffuseSampler0, uv + vec2(offset, 0.0)).r;
    shiftedColor.g = baseColor.g;
    shiftedColor.b = texture(DiffuseSampler0, uv - vec2(offset, 0.0)).b;
    shiftedColor.a = baseColor.a;
    return shiftedColor;
}

// === Main Entry Point ===
void main() {
    vec2 fragCoord = texCoord * resolution;

    vec2 distortedCoord = applyBarrelDistortion(texCoord);
    vec2 distortedFragCoord = distortedCoord * resolution;

    vec4 luma = sampleShrink(distortedFragCoord, 0.5, 0.0);
    luma = blendLuminosity(vec4(0.5), luma);

    vec4 chroma = sampleShrink(distortedFragCoord, 1.0 / 32.0, 3.0);
    chroma = blendColor(luma, chroma);

    OutColor = applyChromaticAberration(distortedCoord, chroma);
}
