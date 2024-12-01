in time;
in vec2 TexCoord;
out vec4 FragColor;

void main() {
    vec4 texColor = texture(Texture, TexCoord);
    vec4 tintColor = vec4(1.0, 0.5, 0.5, 1.0);
    FragColor = texColor * tintColor;
}