/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AdvStructures;

import static AdvStructures.MatchMap._MATCH_TYPE.TYPE_RANDOM;
import static AdvStructures.MatchMap._MATCH_TYPE.TYPE_SMARTSORTED;
import Agents.SolverAgent;
import Challenge.Challenge;
import Common.Logging.ILogManager;
import Environment.cRound;
import auresearch.FactoryHolder;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author d3vil401
 */
public class SmartMatchMap 
{
    private ArrayList<Challenge> _challenges = new ArrayList<>();
    private ArrayList<SolverAgent> _solverAgents = new ArrayList<>();
    private ArrayList<Challenge> _removedCH = new ArrayList<>();
    
    private int importData(ArrayList<Challenge> _challenges, ArrayList<SolverAgent> _solvers)
    {
        int _difference = _challenges.size() - _solvers.size();
        int i = 0;
        if (_difference > 0)
        {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, _difference + " more challenges than solvers.");
            
            for (i = 0; i < (_solvers.size()); i++) {
                this._solverAgents.add(_solvers.get(i));
                this._challenges.add(_challenges.get(i));
            }
            
            for (int k = i + 1; k < _challenges.size(); k++)
                _challenges.get(k)._idledRounds++;
            
        } else if (_difference < 0) {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, _difference + " more solvers than challenges.");
            
            for (i = 0; i < (_challenges.size()); i++) {
                this._solverAgents.add(_solvers.get(i));
                this._challenges.add(_challenges.get(i));
            }
            
            for (int k = i + 1; k < _solvers.size(); k++)
                _solvers.get(k).getStats()._idledRounds++;
            
        } else {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Same amount of agents and solvers.");
            
            for (i = 0; i < (_challenges.size()); i++) {
                this._solverAgents.add(_solvers.get(i));
                this._challenges.add(_challenges.get(i));
            }
            
        }
        
        return _difference;
    }
    
    public void Initialize(ArrayList<Challenge> _challenges, ArrayList<SolverAgent> _solvers, MatchMap._MATCH_TYPE _type)
    {
        switch (_type)
        {
            case TYPE_SMARTSORTED:
                
                
                Collections.sort(_solvers);
                Collections.sort(_challenges);
                
                this.importData(_challenges, _solvers);
                break;
                
            case TYPE_SORTED:
            case TYPE_RANDOM:
            default:
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Matchmaking type unsupported, apparently, this type is only used for smart sorting, use MatchMap for the rest.");
                return;
        }
    }
    
    public SmartMatchMap(ArrayList<Challenge> _challenges, ArrayList<SolverAgent> _solvers)
    {
        this.Initialize(_challenges, _solvers, TYPE_SMARTSORTED);
    }
    
    public int ProcessMatch()
    {
        // Measure as challenges, since those are the ones who are going to decrease over time.
        int i = 0;
        for (i = 0; i < this._challenges.size(); i++)
        {
            if (i > this._challenges.size() || i > this._solverAgents.size()) 
            {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "This should not happen, but one of the indexes is out of range now, check indexing in processMatch of smart sorted algorithm.");
                return -1;
            }
            
            SolverAgent sa = this._solverAgents.get(i);
            Challenge ch = this._challenges.get(i);
            
            if (!ch.isSolved() && 
                    !sa._solvedLastChallenge)
            {
                if (cRound._canProceedWithChallenge(sa, ch)
                        && sa._failedMeet < 3)
                {
                    if (ch.attemptSolve(sa))
                    {
                        ch._idledRounds = 0;
                        ch.forceAssignSuccess(sa);
                        sa.setSolvedLastChallenge(true);
                        sa.getStats()._idledRounds = 0;
                    } else {
                        this._removedCH.add(this._challenges.remove(i));
                        this._challenges.add(this._removedCH.remove(0));
                        i--; // reset to previous position, so for loop increase again by 1 and repeat for same agent.
                    }
                } else {
                    sa.getStats()._idledRounds++;
                    ch._idledRounds++;
                    
                    sa._failedMeet++;
                    
                    this._removedCH.add(this._challenges.remove(i));
                    this._challenges.add(this._removedCH.remove(0));
                    if (!(sa._failedMeet >= 3))
                        i--;
                }
            } else {
                if (FactoryHolder._configManager.getNumberValue("SUBROUND_ITERATIONS") <= 1)
                    FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "There a challenge or a solver who has solved flag active...not supposed to be like this!");
            }
        }
        
        for (int x = 0; x < this._removedCH.size(); x++)
            this._challenges.add(this._removedCH.get(x));
        
        return 0;
    }
}
