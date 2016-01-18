package br.com.techschool.lunarkiller.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Rigidbody {
	// Collision shape properties
	public int collisionType;
	/*
	 * The collision pivot are centered at
	 * Sphere: 		Center of shape
	 * Cylinder: 	Center of the base of shape
	 * Cube: 		Center of the base of shape
	 * Use displacement to change the shape position
	 */
	public float x_size;
	public float y_size;
	public float z_size;
	public float r_size;
	public float x_displacement;
	public float y_displacement;
	public float z_displacement;
	// Body position
	public Vector3 pos;
	
	public Rigidbody(){
		collisionType = PhysConstants.COLLISIONSHAPE_NONE;
		pos = new Vector3(0,0,0);
	}
	
	public Rigidbody(float _r){
		r_size = _r;
		collisionType = PhysConstants.COLLISIONSHAPE_SPHERE;
		x_displacement = 0;
		y_displacement = 0;
		z_displacement = 0;
		pos = new Vector3(0,0,0);
	}
	
	public Rigidbody(float _r, float _h){
		r_size = _r;
		y_size = _h;
		collisionType = PhysConstants.COLLISIONSHAPE_CYLINDER;
		x_displacement = 0;
		y_displacement = 0;
		z_displacement = 0;
		pos = new Vector3(0,0,0);
	}
	
	public Rigidbody(float _x, float _y, float _z){
		x_size = _x;
		y_size = _y;
		z_size = _z;
		collisionType = PhysConstants.COLLISIONSHAPE_CUBE;
		x_displacement = 0;
		y_displacement = 0;
		z_displacement = 0;
		pos = new Vector3(0,0,0);
	}
	
	public boolean isColliding(Rigidbody other){
		// No collision for no shape
		if(this.collisionType == PhysConstants.COLLISIONSHAPE_NONE || other.collisionType == PhysConstants.COLLISIONSHAPE_NONE){
			return false;
		}
		else if(this.collisionType == PhysConstants.COLLISIONSHAPE_SPHERE){
			//Sphere vs Sphere
			if(other.collisionType == PhysConstants.COLLISIONSHAPE_SPHERE){
				return Vector3.dst(this.pos.x + x_displacement, this.pos.y + y_displacement, this.pos.z + z_displacement, other.pos.x + other.x_displacement, other.pos.y + other.y_displacement, other.pos.z + other.z_displacement) < (this.r_size + other.r_size);
			}
			//Sphere vs Cylinder
			else if(other.collisionType == PhysConstants.COLLISIONSHAPE_CYLINDER){
				return (Vector2.dst(this.pos.x + x_displacement, this.pos.z + z_displacement, other.pos.x + other.x_displacement, other.pos.z + other.z_displacement) < (this.r_size + other.r_size)) && 
				((this.pos.y + this.y_displacement + this.r_size > other.pos.y + other.y_displacement) && (this.pos.y + this.y_displacement - this.r_size < other.pos.y + other.y_displacement + (other.y_size)));
			}
			//Sphere vs Cube
			else if(other.collisionType == PhysConstants.COLLISIONSHAPE_CUBE){
				return ((this.pos.x + this.x_displacement + this.r_size > other.pos.x + other.x_displacement - (other.x_size/2)) && (this.pos.x + this.x_displacement - this.r_size < other.pos.x + other.x_displacement + (other.x_size/2))) && 
				((this.pos.z + this.z_displacement + this.r_size > other.pos.z + other.z_displacement - (other.z_size/2)) && (this.pos.z + this.z_displacement - this.r_size < other.pos.z + other.z_displacement + (other.z_size/2))) && 
				((this.pos.y + this.y_displacement + this.r_size > other.pos.y + other.y_displacement) && (this.pos.y + this.y_displacement - this.r_size < other.pos.y + other.y_displacement + (other.y_size)));
			}
			//Sphere vs Other shapes must be implemented here.
			else{
				System.err.println("Can't find Collision Shape");
				return false;
			}
		}
		else if(this.collisionType == PhysConstants.COLLISIONSHAPE_CYLINDER){
			//Cylinder vs Sphere
			if(other.collisionType == PhysConstants.COLLISIONSHAPE_SPHERE){
				return (Vector2.dst(this.pos.x+this.x_displacement, this.pos.z+this.z_displacement, other.pos.x+other.x_displacement, other.pos.z+other.z_displacement) < (this.r_size + other.r_size)) && 
						((this.pos.y+this.y_displacement+this.y_size > other.pos.y + other.y_displacement - other.r_size) && (this.pos.y+this.y_displacement < other.pos.y + other.y_displacement + other.r_size));
			}
			//Cylinder vs Cylinder
			else if(other.collisionType == PhysConstants.COLLISIONSHAPE_CYLINDER){
				return (Vector2.dst(this.pos.x+this.x_displacement, this.pos.z+this.z_displacement, other.pos.x+other.x_displacement, other.pos.z+other.z_displacement) < (this.r_size + other.r_size)) &&
						((this.pos.y+this.y_displacement+this.y_size > other.pos.y + other.y_displacement) && (this.pos.y+this.y_displacement < other.pos.y + other.y_displacement + other.y_size));
			}
			//Cylinder vs Cube
			else if(other.collisionType == PhysConstants.COLLISIONSHAPE_CUBE){
				return((this.pos.x+this.x_displacement+this.r_size > other.pos.x + other.x_displacement - (other.x_size/2)) && (this.pos.x+this.x_displacement-this.r_size < other.pos.x + other.x_displacement + (other.x_size/2))) &&
						((this.pos.z+this.z_displacement+this.r_size > other.pos.z + other.z_displacement - (other.z_size/2)) && (this.pos.z+this.z_displacement-this.r_size < other.pos.z + other.z_displacement + (other.z_size/2)))&&
						((this.pos.y+this.y_displacement+this.y_size > other.pos.y + other.y_displacement) && (this.pos.y+this.y_displacement < other.pos.y + other.y_displacement + other.y_size));
			}
			//Cylinder vs Other shapes must be implemented here.
			else{
				System.err.println("Can't find Collision Shape");
				return false;
			}
		}
		else if(this.collisionType == PhysConstants.COLLISIONSHAPE_CUBE){
			//Cube vs Sphere
			if(other.collisionType == PhysConstants.COLLISIONSHAPE_SPHERE){
				return(this.pos.x+this.x_displacement-(this.x_size/2) < (other.pos.x+other.x_displacement+other.r_size) && this.pos.x+this.x_displacement+(this.x_size/2) > (other.pos.x+other.x_displacement-other.r_size))&&
						(this.pos.y+this.y_displacement < (other.pos.y+other.y_displacement+other.r_size) && this.pos.y+this.y_displacement+(this.y_size) > (other.pos.y+other.y_displacement-other.r_size))&&
						(this.pos.z+this.z_displacement-(this.z_size/2) < (other.pos.z+other.z_displacement+other.r_size) && this.pos.z+this.z_displacement+(this.z_size/2) > (other.pos.z+other.z_displacement-other.r_size));
			}
			//Cube vs Cylinder
			else if(other.collisionType == PhysConstants.COLLISIONSHAPE_CYLINDER){
				return(this.pos.x+this.x_displacement-(this.x_size/2) < (other.pos.x+other.x_displacement+other.r_size) && this.pos.x+this.x_displacement+(this.x_size/2) > (other.pos.x+other.x_displacement-other.r_size))&&
						(this.pos.y+this.y_displacement < other.pos.y+other.y_displacement+other.y_size && this.pos.y+this.y_displacement+this.y_size > other.pos.y+other.y_displacement)&&
						(this.pos.z+this.z_displacement-(this.z_size/2) < (other.pos.z+other.z_displacement+other.r_size) && this.pos.z+this.z_displacement+(this.z_size/2) > (other.pos.z+other.z_displacement-other.r_size));
			}
			//Cube vs Cube
			else if(other.collisionType == PhysConstants.COLLISIONSHAPE_CUBE){
				return(this.pos.x+this.x_displacement-(this.x_size/2)<other.pos.x+other.x_displacement+(other.x_size/2) && this.pos.x+this.x_displacement+(this.x_size/2)>other.pos.x+other.x_displacement-(other.x_size/2))&&
						(this.pos.y+this.y_displacement<other.pos.y+other.y_displacement+other.y_size && this.pos.y+this.y_displacement+this.y_size>other.pos.y+other.y_displacement)&&
						(this.pos.z+this.z_displacement-(this.z_size/2)<other.pos.z+other.z_displacement+(other.x_size/2) && this.pos.z+this.z_displacement+(this.z_size/2)>other.pos.z+other.z_displacement-(other.z_size/2));
			}
			//Cube vs Other shapes must be implemented here.
			else{
				System.err.println("Can't find Collision Shape");
				return false;
			}
		}
		return false;
	}
	
	// Return a vector that points against the face of collision, or a null vector in case of no collision
	public Vector3 getNormal(Rigidbody other){
		if(this.collisionType == PhysConstants.COLLISIONSHAPE_NONE || other.collisionType == PhysConstants.COLLISIONSHAPE_NONE){
			return new Vector3(0,0,0);
		}
		else if(other.collisionType == PhysConstants.COLLISIONSHAPE_SPHERE){
			return this.pos.add(x_displacement, y_displacement, z_displacement).sub(other.pos.add(other.x_displacement, other.y_displacement, other.z_displacement)).nor();
		}
		else if(other.collisionType == PhysConstants.COLLISIONSHAPE_CYLINDER){
			if(this.pos.y+this.y_displacement > other.pos.y+other.y_displacement && this.pos.y+this.y_displacement < other.pos.y+other.y_displacement+other.y_size){
				Vector3 v = this.pos.add(x_displacement, y_displacement, z_displacement).sub(other.pos.add(other.x_displacement, other.y_displacement, other.z_displacement));
				v.y=0;
				return v.nor();
			}
			else if(this.pos.y+this.y_displacement > other.pos.y+other.y_displacement){
				return new Vector3(0,1,0);
			}
			else{
				return new Vector3(0,-1,0); 
			}
		}
		else if(other.collisionType == PhysConstants.COLLISIONSHAPE_CUBE){
			Vector3 v = this.pos.add(x_displacement, y_displacement, z_displacement).sub(other.pos.add(other.x_displacement, other.y_displacement, other.z_displacement));
			if(Math.abs(v.x) >= Math.abs(v.y) && Math.abs(v.x) >= Math.abs(v.z)){
				if(v.x>=0){
					return new Vector3(1,0,0);
				}
				else{
					return new Vector3(-1,0,0);
				}
			}
			else if(Math.abs(v.y) >= Math.abs(v.x) && Math.abs(v.y) >= Math.abs(v.z)){
				if(v.y>=0){
					return new Vector3(0,1,0);
				}
				else{
					return new Vector3(0,-1,0);
				}
			}
			else if(Math.abs(v.z) >= Math.abs(v.y) && Math.abs(v.z) >= Math.abs(v.x)){
				if(v.z>=0){
					return new Vector3(0,0,1);
				}
				else{
					return new Vector3(0,0,-1);
				}
			}
			else{
				return new Vector3(0,0,0);
			}
		}
		else{
			System.err.println("Can't find Collision Shape");
			return new Vector3(0,0,0);
		}
	}
}
