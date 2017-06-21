/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AdvStructures;

import static AdvStructures.MatchMap._MATCH_TYPE.TYPE_RANDOM;
import Agents.SolverAgent;
import Challenge.Challenge;
import Common.Logging.ILogManager;
import Environment.cRound;
import auresearch.FactoryHolder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author d3vil401
 */
public class MatchMap implements Map<Challenge, SolverAgent>
{
    public enum _MATCH_TYPE 
    {
        TYPE_SORTED,
        TYPE_SMARTSORTED,
        TYPE_RANDOM
    }
    
    public MatchMap()
    {
        
    }
    
    public MatchMap(ArrayList<Challenge> _challenges, ArrayList<SolverAgent> _solvers, _MATCH_TYPE _type)
    {
        this.Initialize(_challenges, _solvers, _type);
    }
    
    private final Map<Challenge, SolverAgent> _map = new HashMap<>();
    private _MATCH_TYPE _matchType = TYPE_RANDOM;
    
    private int importData(ArrayList<Challenge> _challenges, ArrayList<SolverAgent> _solvers)
    {
        int _difference = _challenges.size() - _solvers.size();
        int i = 0;
        if (_difference > 0)
        {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, _difference + " more challenges than solvers.");
            
            for (i = 0; i < (_solvers.size()); i++)
                this._map.put(_challenges.get(i), _solvers.get(i));
            
            for (int k = i; k < _challenges.size(); k++)
                _challenges.get(k)._idledRounds++;
            
        } else if (_difference < 0) {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, _difference + " more solvers than challenges.");
            
            for (i = 0; i < (_challenges.size()); i++)
                this._map.put(_challenges.get(i), _solvers.get(i));
            
            for (int k = i; k < _solvers.size(); k++)
                _solvers.get(k).getStats()._idledRounds++;
            
        } else {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Same amount of agents and solvers.");
            
            for (i = 0; i < _challenges.size(); i++)
                this._map.put(_challenges.get(i), _solvers.get(i));
            
        }
        
        return _difference;
    }
    
    @Override
    public SolverAgent get(Object key) 
    {
        return this._map.get(key);
    }

    @Override
    public SolverAgent remove(Object key) 
    {
        return this._map.remove(key);
    }
    
    public void Initialize(ArrayList<Challenge> _challenges, ArrayList<SolverAgent> _solvers, _MATCH_TYPE _type)
    {
        switch (_type)
        {
            case TYPE_SMARTSORTED:
            case TYPE_SORTED:
                Collections.sort(_solvers);
                Collections.sort(_challenges);
                
                this.importData(_challenges, _solvers);
                break;
            case TYPE_RANDOM:
                
                Collections.shuffle(_solvers);
                Collections.shuffle(_challenges);
                
                this.importData(_challenges, _solvers);
                break;
                
            default:
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Matchmaking type unsupported, apparently.");
                return;
        }
        this._matchType = _type;
    }
    
    public MatchMap(ArrayList<Challenge> _challenges, ArrayList<SolverAgent> _solvers)
    {
        this.Initialize(_challenges, _solvers, TYPE_RANDOM);
    }
    
    @Override
    public int size() 
    {
        return this._map.size();
    }

    @Override
    public boolean isEmpty() 
    {
        return this._map.isEmpty();
    }
    
    public boolean containsChallenge(Challenge challenge)
    {
        for (Map.Entry pair : this._map.entrySet()) {
            Challenge ch = (Challenge)pair.getKey();
            if (ch.equals(challenge))
                return true;
        }
        
        return false;
    }
    
    public boolean containsSolver(SolverAgent solver)
    {
        for (Map.Entry pair : this._map.entrySet()) {
            SolverAgent sa = (SolverAgent)pair.getValue();
            if (sa.equals(solver))
                return true;
        }
        
        return false;
    }

    @Override
    public boolean containsKey(Object key) 
    {
        return this._map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) 
    {
        return this._map.containsValue(value);
    }

    @Override
    public void putAll(Map m) 
    {
        this._map.putAll(m);
    }

    @Override
    public void clear() 
    {
        this._map.clear();
    }

    @Override
    public Set keySet() 
    {
        return this._map.keySet();
    }

    @Override
    public Collection values() 
    {
        return this._map.values();
    }

    @Override
    public Set entrySet() 
    {
        return this._map.entrySet();
    }

    @Override
    public SolverAgent put(Challenge key, SolverAgent value) 
    {
        return this._map.put(key, value);
    }
    
    public int ProcessMatch()
    {
        for (Map.Entry pair : this._map.entrySet()) 
        {
            SolverAgent sa = (SolverAgent)pair.getValue();
            Challenge ch = (Challenge)pair.getKey();
            
            if (!ch.isSolved() && 
                    !sa._solvedLastChallenge && 
                    !sa._solvedLastChallenge)
            {
                if (cRound._canProceedWithChallenge(sa, ch))
                {
                    if (ch.attemptSolve(sa))
                    {
                        ch._idledRounds = 0;
                        ch.forceAssignSuccess(sa);
                        sa.setSolvedLastChallenge(true);
                        sa.getStats()._idledRounds = 0;
                    } else {
                        if (this._matchType == _MATCH_TYPE.TYPE_SMARTSORTED)
                        {
                            // shuffle up challenges in map...
                            
                        } else {
                            sa.getStats()._idledRounds++;
                            sa.setTryHarder(sa.getTryHarded() + 1);
                        }
                    }
                } else {
                    sa.getStats()._idledRounds++;
                    ch._idledRounds++;
                }
            } else 
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "There a challenge or a solver who has solved flag active...not supposed to be like this!");
        }
        
        return 0;
    }
    
}
