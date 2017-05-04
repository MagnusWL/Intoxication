/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.movement;

import group04.collisioncommon.ICollisionService;
import group04.common.GameData;
import group04.common.World;
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
public class ProcessorTest {
    
    public ProcessorTest() {
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
     * Test of pickUpItemEvent method, of class Processor.
     */
    @Test
    public void testPickUpItemEvent() {
        System.out.println("pickUpItemEvent");
        World world = null;
        ICollisionService e = null;
        Processor instance = new Processor();
        instance.pickUpItemEvent(world, e);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of collision method, of class Processor.
     */
    @Test
    public void testCollision() {
        System.out.println("collision");
        World world = null;
        GameData gameData = null;
        ICollisionService e = null;
        Processor instance = new Processor();
        instance.collision(world, gameData, e);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    
}
