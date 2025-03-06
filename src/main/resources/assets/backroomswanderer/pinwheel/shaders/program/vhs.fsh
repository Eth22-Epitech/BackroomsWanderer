#version 150

uniform sampler2D DiffuseSampler0;
uniform float time;

in vec2 texCoord;
out vec4 OutColor;

void chromaticAberration(in vec2 texCoord, out vec4 color) {
    // Calculate the distance from the center of the screen (texCoord is normalized)
    vec2 center = vec2(0.5, 0.5); // Center of the screen
    float dist = distance(texCoord, center); // Distance from the center

    // Define the maximum offset values for chromatic aberration (100% intensity at the edges)
    float maxOffset = 0.01; // Maximum offset value (100% intensity)

    // Scale the offsets based on the distance from the center
    float rOffset = maxOffset * dist; // Red channel offset based on distance
    float bOffset = maxOffset * dist; // Blue channel offset based on distance

    // Slightly refine the offsets to ensure better alignment and smoother transition
    vec2 rTexCoord = texCoord + vec2(rOffset, 0.0);  // Red channel offset
    vec2 bTexCoord = texCoord + vec2(-bOffset, 0.0); // Blue channel offset (opposite direction)

    // Sample the texture for each color channel with slight offsets in the texture coordinates
    color.r = texture(DiffuseSampler0, rTexCoord).r;  // Red channel offset
    color.g = texture(DiffuseSampler0, texCoord).g;   // Green channel remains the same
    color.b = texture(DiffuseSampler0, bTexCoord).b;  // Blue channel offset
    color.a = texture(DiffuseSampler0, texCoord).a;   // Alpha remains the same
}

void main() {
    vec4 color;

    // Call the chromatic aberration function
    chromaticAberration(texCoord, color);

    // Set the output color with the distorted texture
    OutColor = color;
}
