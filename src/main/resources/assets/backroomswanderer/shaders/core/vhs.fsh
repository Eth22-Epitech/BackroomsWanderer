#version 150

uniform sampler2D DiffuseSampler;
uniform float time;
in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 uv = texCoord;

    // Horizontal scanlines effect based on time
    float scanline = sin(uv.y * 480.0 + time * 10.0) * 0.02;

    // Vertical offset for jitter
    uv.x += scanline;
    uv.y += sin(time * 3.0) * 0.002;

    // Apply chromatic aberration with slight offset per color channel
    vec3 col;
    col.r = texture(DiffuseSampler, uv + vec2(0.002, 0.0)).r;
    col.g = texture(DiffuseSampler, uv).g;
    col.b = texture(DiffuseSampler, uv - vec2(0.002, 0.0)).b;

    // Add noise to simulate VHS effect
    float noise = (fract(sin(dot(uv + time, vec2(12.9898,78.233))) * 43758.5453) - 0.5) * 0.05;
    col += vec3(noise);

    fragColor = vec4(col, 1.0);
}
