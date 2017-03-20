/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Agents.Group.Group;
import Agents.Group._MODEL_SETUP;
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
public class groupsTest {
    
    public groupsTest() 
    {
        
    }
    
    @Test
    public void testGroups() throws IOException
    {
        FactoryHolder._logManager = new cLogConsole();
        FactoryHolder._configManager = new ConfigManager();
        FactoryHolder._logManager.initializeSession("session");
        FactoryHolder._logManager.setDebugMode(false);
        
        int _runs = 100;
        
        for (int x = 0; x < _runs; x++)
        {
            ArrayList<SolverAgent> _solvers = new ArrayList();
            for (int i = 0; i < 50; i++)
                _solvers.add(new SolverAgent());

            ProposerAgent _proposer = new ProposerAgent();
            _proposer._generateProblem();

            Group _group = new Group(_solvers, _proposer.getChallengeProposed());
            if (_group.getMembersCount() >= 1)
            {
                System.out.println("Total Experience: " + _group.getTotalExperience());
            } else {
                System.out.println(_group + " disbanded (no members)");
                _group.disband();
            }

            _group.attemptSolve();
        }
    }
}
