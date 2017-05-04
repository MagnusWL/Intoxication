/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.player;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.GameKeys;
import static group04.common.GameKeys.SPACE;
import group04.common.World;
import group04.playercommon.PlayerEntity;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Josan gamle stodder
 */
public class PlayerSystemTest {

    public PlayerSystemTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of playerMovement method, of class PlayerSystem.
     */
    @Test
    public void testPlayerMovement() {
        //For jumping
        //Arrange
        PlayerEntity player = new PlayerEntity();

        float expected = 100;
        GameData gameData = new GameData();
        World world = new World();
        boolean actual;

        player.setJumpSpeed(expected);
        player.setGrounded(true);
        player.setIdleAnimation("idle_animation");
        player.setJumpAnimation("jump_animation");

        PlayerSystem instance = new PlayerSystem();
        //Act
        instance.playerMovement(player, gameData, world);
        actual = expected == player.getVerticalVelocity();
        
        //Assert
        assertFalse(actual);
        
        //Act
        gameData.getKeys().setKey(GameKeys.SPACE, true);
        instance.playerMovement(player, gameData, world);

        actual = expected == player.getVerticalVelocity();
        //Assert
        assertTrue(actual);

        //For moveing with KEYS.D Should be the same as KEYS.A.
        //Arrange
        player.setCurrentAnimation("idle_animation");
        player.setRunAnimation("run_animation");
        expected = 50;
        player.setMovementSpeed(expected);
        
        //Act
        instance.playerMovement(player, gameData, world);
        actual = expected == player.getVelocity();
        //Assert
        assertFalse(actual);
        //Act
        gameData.getKeys().setKey(GameKeys.D, true);
        instance.playerMovement(player, gameData, world);
        actual = expected == player.getVelocity();
        //Assert
        assertTrue(actual);
    }
}
