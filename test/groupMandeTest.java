/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Agents.Group.GroupManager;
import Agents.ProposerAgent;
import Agents.SolverAgent;
import Challenge.Challenge;
import Common.Configuration.ConfigManager;
import Common.Logging.cLogConsole;
import auresearch.FactoryHolder;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author d3vil401
 */
public class groupMandeTest {
    
    public groupMandeTest() 
    {
        
    }
    
    
    @Test
    public void testGroupManager() throws IOException
    {
        FactoryHolder._logManager = new cLogConsole();
        FactoryHolder._configManager = new ConfigManager();
        FactoryHolder._logManager.initializeSession("session");
        FactoryHolder._logManager.setDebugMode(true);
        
        ArrayList<SolverAgent> _solvers = new ArrayList();
        ArrayList<ProposerAgent> _proposers = new ArrayList();
        ArrayList<Challenge> _challenges = new ArrayList<>();
        
        for (int i = 0; i < 100; i++)
            _solvers.add(new SolverAgent());

        for (int i = 0; i < 85; i++) {
            _proposers.add(new ProposerAgent());
            _proposers.get(_proposers.size() - 1)._generateProblem();
        }

        for (int i = 0; i < 85; i++)
            _challenges.add(_proposers.get(i).getChallengeProposed());
        
        System.out.println("Creating groups...");
        GroupManager _gm = new GroupManager(_solvers, _challenges, 0);
    }
}
