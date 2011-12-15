//  
//  Player.java
//  MyCraft
//  
//  Created on 09/12/2011.
//  Copyright (c) 2011 Mitchell Kember. All rights reserved.
//
//  This software is provided 'as-is', without any express or implied
//  warranty. In no event will the authors be held liable for any damages
//  arising from the use of this software.
//  
//  Permission is granted to anyone to use this software for any purpose,
//  including commercial applications, and to alter it and redistribute it
//  freely, subject to the following restrictions:
//  
//  1. The origin of this software must not be misrepresented; you must not
//  claim that you wrote the original software. If you use this software
//  in a product, an acknowledgment in the product documentation would be
//  appreciated but is not required.
//  
//  2. Altered source versions must be plainly marked as such, and must not be
//  misrepresented as being the original software.
//  
//  3. This notice may not be removed or altered from any source
//  distribution.
//  

package com.hecticcraft.mycraft;

/**
 * Player represents the user in the MyCraft world. A Player is primarily a view
 * into the MyCraft world, and so much of the work is done by the Camera which
 * the Player owns. This class also manages a Player's movements and physics.
 * 
 * @author Mitchell Kember
 * @since 09/12/2011
 */
public class Player {
    // done documenting: mycraft, controller, camera, vector, gamestateinputdata
    
    /**
     * The number of units above this Player's feet that the head or Camera
     * is stationed.
     */
    private static final float CAMERA_HEIGHT = 1.5f;
    
    /**
     * Speed in units per 60 FPS frame for this Player's movement.
     */
    private static final float MOVE_SPEED = 0.075f;
    
    /**
     * The pull of gravity, in units per 60 FPS frame.
     */
    private static final float GRAVITY = -0.015f;
    
    /**
     * The initial upward velocity this Player will have upon jumping.
     */
    private static final float INITAL_JUMP_VELOCITY = 0.25f;
    
    /**
     * Whether this Player is on something solid. If false, this Player is in
     * the air (either jumping or falling).
     */
    private boolean isGrounded = false;
    
    /**
     * The height of what the Player is currently standing on.
     */
    private float ground = 0;
    
    /**
     * The height of this Player; this Player's Y coordinate in 3D space where
     * positive Y is upwards.
     */
    private float height = 0;
    
    /**
     * The vertical velocity of this Player, used for jumping and falling.
     */
    private float velocity = 0;
    
    /**
     * The view of this Player into the MyCraft world.
     */
    private Camera camera = new Camera();
    
    {
        camera.setPositionY(height+CAMERA_HEIGHT);
    }
    
    private Vector deltaPosition;
    
    /**
     * Causes this Player to jump, unless this Player is already in the air
     * (jumping or falling) in which case nothing happens.
     */
    void jump() {
        if (height == ground) {
            ground = 0;
            height += 0.0001f;
            isGrounded = false;
            velocity = INITAL_JUMP_VELOCITY;
        }
    }
    
    /**
     * Checks for collision with blocks and moves the Camera accordingly.
     * 
     * @param chunk the Chunk this Player is in
     */
    void collision(Chunk chunk) {
        // Boundaries
        Vector position = camera.getPosition();
        if (position.x < 0) camera.setPositionX(0);
        else if (position.x > 8) camera.setPositionX(8);
        if (position.z < 0) camera.setPositionZ(0);
        else if (position.z > 8) camera.setPositionZ(8);
        
        try {
            if (deltaPosition.x > 0) {
                if (chunk.getBlockType(new Block((int)Math.round(position.x), (int)(position.y-CAMERA_HEIGHT), (int)(position.z-0.25f))) != 0
                        || chunk.getBlockType(new Block((int)Math.round(position.x), (int)(position.y-CAMERA_HEIGHT), (int)(position.z+0.25f))) != 0
                        || chunk.getBlockType(new Block((int)Math.round(position.x), (int)(position.y-CAMERA_HEIGHT+1), (int)(position.z-0.25f))) != 0
                        || chunk.getBlockType(new Block((int)Math.round(position.x), (int)(position.y-CAMERA_HEIGHT+1), (int)(position.z+0.25f))) != 0) {
                    camera.setPositionX((int)Math.round(position.x) - 0.5f);
                }
            } else {
                if ((chunk.getBlockType(new Block((int)Math.round(position.x)-1, (int)(position.y-CAMERA_HEIGHT), (int)(position.z-0.25f))) != 0
                        || chunk.getBlockType(new Block((int)Math.round(position.x)-1, (int)(position.y-CAMERA_HEIGHT), (int)(position.z+0.25f))) != 0
                        || chunk.getBlockType(new Block((int)Math.round(position.x)-1, (int)(position.y-CAMERA_HEIGHT+1), (int)(position.z-0.25f))) != 0
                        || chunk.getBlockType(new Block((int)Math.round(position.x)-1, (int)(position.y-CAMERA_HEIGHT+1), (int)(position.z+0.25f))) != 0)) {
                    camera.setPositionX((int)Math.round(position.x) + 0.5f);
                }
            }
            
            if (deltaPosition.z > 0) {
                if (chunk.getBlockType(new Block((int)(position.x-0.25f), (int)(position.y-CAMERA_HEIGHT), (int)Math.round(position.z))) != 0
                        || chunk.getBlockType(new Block((int)(position.x+0.25f), (int)(position.y-CAMERA_HEIGHT), (int)Math.round(position.z))) != 0
                        || chunk.getBlockType(new Block((int)(position.x-0.25f), (int)(position.y-CAMERA_HEIGHT+1), (int)Math.round(position.z))) != 0
                        || chunk.getBlockType(new Block((int)(position.x+0.25f), (int)(position.y-CAMERA_HEIGHT+1), (int)Math.round(position.z))) != 0) {
                    camera.setPositionZ((int)Math.round(position.z) - 0.5f);
                }
            } else {
                if (chunk.getBlockType(new Block((int)(position.x-0.25f), (int)(position.y-CAMERA_HEIGHT), (int)Math.round(position.z)-1)) != 0
                        || chunk.getBlockType(new Block((int)(position.x+0.25f), (int)(position.y-CAMERA_HEIGHT), (int)Math.round(position.z)-1)) != 0
                        || chunk.getBlockType(new Block((int)(position.x-0.25f), (int)(position.y-CAMERA_HEIGHT+1), (int)Math.round(position.z)-1)) != 0
                        || chunk.getBlockType(new Block((int)(position.x+0.25f), (int)(position.y-CAMERA_HEIGHT+1), (int)Math.round(position.z)-1)) != 0) {
                    camera.setPositionZ((int)Math.round(position.z) + 0.5f);
                }
            }
            /*
            if (chunk.getBlockType(new Block((int)Math.round(position.x), (int)(position.y-CAMERA_HEIGHT)-1, (int)(position.z))) != 0) {
                ground = ((int)position.y-CAMERA_HEIGHT)-1;
            }*/
        } catch (ArrayIndexOutOfBoundsException aioobe) {}
        // request adjacent chunk...
    }
    
    /**
     * Moves this Player and orients this Player's view according to user input.
     * 
     * @param input the user input
     * @param multiplier 
     */
    void move(GameStateInputData input, float multiplier) {
        Vector previousPosition = camera.getPosition();
        // Movement
        if (input.forward) {
            camera.moveForward(MOVE_SPEED * multiplier);
        }
        if (input.backward) {
            camera.moveForward(-MOVE_SPEED * multiplier);
        }
        if (input.left) {
            camera.strafeRight(-MOVE_SPEED * multiplier);
        }
        if (input.right) {
            camera.strafeRight(MOVE_SPEED * multiplier);
        }
        
        if (height != ground) {
            height += velocity * multiplier;
            velocity += GRAVITY * multiplier;
            
            if (height < ground) {
                height = ground;
                velocity = 0;
                isGrounded = true;
            }
            
            camera.setPositionY(height+CAMERA_HEIGHT);
        }
        
        deltaPosition = camera.getPosition().minus(previousPosition);
        
        // Orient the camera
        camera.pitch(input.lookDeltaY);
        camera.yaw(input.lookDeltaX);
        
        
    }
    
    /**
     * Gets this Player's Camera object.
     * 
     * @return the camera
     */
    Camera getCamera() {
        return camera;
    }
}
