/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.gravity;

import group04.common.Entity;
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
     * Test of process method, of class Processor.
     */
    @org.junit.Test
    public void testIfGravityWorks() {
        GameData gameData = new GameData();
        World world = new World();

        Entity affectByGravityEntity = new Entity();
        Entity doNotAffectByGravityEntity = new Entity();

        boolean actual;

        affectByGravityEntity.setVerticalVelocity(10);
        affectByGravityEntity.setHasGravity(true);
        doNotAffectByGravityEntity.setVerticalVelocity(10);
        doNotAffectByGravityEntity.setHasGravity(false);

        world.addEntity(affectByGravityEntity);
        world.addEntity(doNotAffectByGravityEntity);

        gameData.setDelta(100);

        Processor instance = new Processor();
        instance.process(gameData, world);

        if (affectByGravityEntity.getVerticalVelocity() < 10) {
            actual = true;
        } else {
            actual = false;
        }
        assertTrue(actual); // TODO review the generated test code and remove the default call to fail.

        if (doNotAffectByGravityEntity.getVerticalVelocity() == 10) {
            actual = true;
        } else {
            actual = false;
        }
        assertTrue(actual);
    }
}
