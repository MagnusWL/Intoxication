/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.enemy;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;
import group04.enemycommon.EnemyEntity;
import group04.enemycommon.EnemyType;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author burno
 */
public class EnemySystemTest {

    public EnemySystemTest() {
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
     * Test of createEnemy method, of class EnemySystem.
     */
    @org.junit.Test
    public void testCreateEnemy() {

        // Arrange
        System.out.println("createEnemy test");
        GameData gameData = new GameData();
        int[] intArray = new int[3];
        gameData.getSpriteInfo().put("enemybeer_run_animation", intArray);
        gameData.getSpriteInfo().put("enemynarko_run_animation", intArray);
        World world = new World();
        List<Entity> testList;
        int x = 1;
        int y = 1;
        int currentLevel = 2;
        EnemySystem instance = new EnemySystem();
        EnemyType beerEnemyType = EnemyType.BEER;
        EnemyType narkoEnemyType = EnemyType.NARKO;

        // Act part 1
        instance.createEnemy(gameData, world, 1, 1, EnemyType.BEER, 2);
        instance.createEnemy(gameData, world, x, y, beerEnemyType, currentLevel);
        testList = world.getEntities(EnemyEntity.class);
        EnemyEntity enemy1 = (EnemyEntity) testList.get(0);
        EnemyEntity enemy2 = (EnemyEntity) testList.get(1);

        // Asserting the created enemy is a beer enemy
        assertEquals(enemy1.getEnemyType(), beerEnemyType);

        // Assert the new created enemy is also a beer enemy
        assertTrue(enemy1.equals(enemy2));

        // Act part 2
        world = new World();
        instance.createEnemy(gameData, world, 3, 4, narkoEnemyType, 3);
        instance.createEnemy(gameData, world, 1, 1, EnemyType.BEER, 2);
        testList = world.getEntities(EnemyEntity.class);
        enemy1 = (EnemyEntity) testList.get(0);
        enemy2 = (EnemyEntity) testList.get(1);
        
        
        // Assert not equals that enemy1.type is equal to enemy3.type)
        assertNotEquals(enemy1.getEnemyType(), enemy2.getEnemyType());

    }
}
