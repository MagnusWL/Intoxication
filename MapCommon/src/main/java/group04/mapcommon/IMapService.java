/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.mapcommon;

import group04.common.GameData;
import group04.common.World;

/**
 *
 * @author Michael-PC
 */
public interface IMapService {
    public void process(GameData gameData, World world, String map);
}
