/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multithreading;

import Common.Logging.ILogManager;
import Environment.RoundManager;
import Environment.roundStatsHolder;
import auresearch.FactoryHolder;
import java.util.ArrayList;

/**
 *
 * @author d3vil401
 */
public class MRThreading implements Runnable
{
    private ArrayList<RoundManager> _roundManager = new ArrayList<>();
    private int                     _turnCounter = 0;
    
    public MRThreading()
    {
        this.spawnData();
    }
    
    private synchronized void spawnData()
    {
        for (int i = 0; i < 5; i++) {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Firing up RM#" + i + ".");
            this._roundManager.add(new RoundManager (
                                                FactoryHolder._configManager.getNumberValue("MAX_ROUNDS"),
                                                FactoryHolder._configManager.getNumberValue("SOLVER_AGENTS_AMOUNT"),
                                                FactoryHolder._configManager.getNumberValue("PROPOSER_AGENTS_AMOUNT")
                                    )
                                  );
        }
    }
    
    public synchronized int getTotalSAgents()
    {
        return FactoryHolder._configManager.getNumberValue("SOLVER_AGENTS_AMOUNT") * this._roundManager.size();
    }
    
    public synchronized int getTotalPAgents()
    {
        return FactoryHolder._configManager.getNumberValue("PROPOSER_AGENTS_AMOUNT") * this._roundManager.size();
    }
    
    public synchronized int currentTurn()
    {
        return this._turnCounter;
    }
    
    public synchronized boolean ended()
    {
        if (this._turnCounter >= 5)
            return true;
        
        return false;
    }
    
    public synchronized ArrayList<ArrayList<roundStatsHolder>> getAllLocalStats()
    {
        ArrayList<ArrayList<roundStatsHolder>> _localStats = new ArrayList();
        
        for (int i = 0; i < 5; i++)
            _localStats.add(this.getLocalStatsForSimulation(i));
        
        return _localStats;
    }
    
    public synchronized ArrayList<roundStatsHolder> getLocalStatsForSimulation(int _simNumber)
    {
        if (_simNumber >= 0 && _simNumber <= 5)
            return this._roundManager.get(_simNumber).getLocalStats();
        else
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Simulation number requested out of allowed range.");
        
        return null;
    }
    
    public synchronized ArrayList<roundStatsHolder> getStatistics()
    {
        ArrayList<roundStatsHolder> _stats = new ArrayList<>();
        
        for (int i = 0; i < this._roundManager.size(); i++)
            _stats.add(this._roundManager.get(i).getGlobalStats());
        
        return _stats;
    }
    
    @Override
    public void run() 
    {   
        while (this._turnCounter < 5)
        {
            this._roundManager.get(this._turnCounter).runLoop();
            if (!this._roundManager.get(this._turnCounter).hasNext()) {
                this._roundManager.get(this._turnCounter).end();
                this._turnCounter++;
            }
        }
    }
    
}
