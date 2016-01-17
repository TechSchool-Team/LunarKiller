attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

uniform mat4 u_worldTrans; // Our modelinstance transform
uniform mat4 u_projViewTrans; //our camera projection matrix
uniform mat3 u_normalMatrix; //our normal matrix

uniform vec3 u_directionalColour;
uniform vec3 u_directionalDirection;

varying vec2 v_texCoords;

varying vec3 v_directionalColour;
varying vec3 v_directionalDirection;

varying vec3 v_vertexNormal;

void main () {

    v_texCoords = a_texCoord0; //we need to pass on our texture coordinates to the frag shader

    vec4 screenSpacePosition = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);

    v_directionalColour = u_directionalColour;
    v_directionalDirection = u_directionalDirection;

    v_vertexNormal = normalize(u_normalMatrix * a_normal);

    gl_Position = screenSpacePosition;
}
