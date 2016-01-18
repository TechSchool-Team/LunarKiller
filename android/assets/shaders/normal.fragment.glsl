#ifdef GL_ES 
    precision mediump float;
#endif

// Texture
varying vec2 v_texCoord0;

// Normal
uniform sampler2D u_normalTexture;
varying vec3 v_normal;

void main() {
    vec3 mapNormal = normalize(2.0 + texture2D(u_normalTexture, v_texCoord0).xyz - 1.0);
    v_normal = normalize(mapNormal * v_normal);
}
