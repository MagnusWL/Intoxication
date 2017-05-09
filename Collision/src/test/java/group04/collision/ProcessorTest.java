/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.collision;

import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import group04.mapcommon.MapEntity;
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
     * Test of isEntitiesColliding method, of class Processor.
     */
    @Test
    public void testIsEntitiesColliding() {
        //Arrange
        Entity entity1 = new Entity();
        Entity entity2 = new Entity();
        CollisionSystem instance = new CollisionSystem();
        boolean expResult = true;

        //Act
        entity1.setShapeX(new float[]{100f, 200f, 100f, 200f});
        entity1.setShapeY(new float[]{100f, 100f, 200f, 200f});
        entity1.setX(100);
        entity1.setY(100);
        entity2.setShapeX(new float[]{120f, 400f, 120f, 400f});
        entity2.setShapeY(new float[]{120f, 120f, 400f, 400f});
        entity2.setX(100);
        entity2.setY(100);

        boolean result = instance.isEntitiesColliding(entity1, entity2);

        //Assert true when colliding
        assertEquals(expResult, result);
        //Arrange
        expResult = false;
        
        //Act
        entity1.setX(500);
        result = instance.isEntitiesColliding(entity1, entity2);

        //Assert false when not colliding
        assertEquals(expResult, result);
       }

}
