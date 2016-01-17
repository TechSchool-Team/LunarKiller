#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_diffuseTexture;
uniform sampler2D u_normalTexture;

varying vec2 v_texCoords;

varying vec3 v_directionalColour;
varying vec3 v_directionalDirection;

varying vec3 v_vertexNormal;


void main () {
    vec4 diffuse = texture2D(u_diffuseTexture, v_texCoords);
    vec3 adjustedNormal = normalize(2.0 + texture2D(u_normalTexture, v_texCoords).xyz - 1.0);
    adjustedNormal *= v_vertexNormal;

    //we just add on the lighting term to our diffuse

    //We do per fragment lighting here
    vec3 lightDirection = -v_directionalDirection;
    float NdotL = clamp(dot(adjustedNormal, lightDirection), 0.0, 1.0); //we do a calculation based on the vertex normal and the light direction, and clamp it between 0,1

    vec3 lightTerm = v_directionalColour * NdotL;

    diffuse.rgb *= lightTerm;

    vec3 ambientLight = vec3(0.4, 0.4, 0.4);

    diffuse.rgb += ambientLight;

    gl_FragColor = diffuse;

}
