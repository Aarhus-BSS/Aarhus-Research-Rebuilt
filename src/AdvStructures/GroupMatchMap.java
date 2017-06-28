/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AdvStructures;

import Agents.Group.Group;
import Agents.SolverAgent;
import Challenge.Challenge;
import Common.Logging.ILogManager;
import auresearch.FactoryHolder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author d3vil401
 */
public class GroupMatchMap implements Map<Challenge, ArrayList<Group>>
{

    @Override
    public ArrayList<Group> get(Object key) 
    {
        return this._map.get(key);
    }

    @Override
    public ArrayList<Group> put(Challenge key, ArrayList<Group> value) 
    {
        return this._map.put(key, value);
    }

    @Override
    public ArrayList<Group> remove(Object key)
    {
        return this._map.remove(key);
    }
    
    public GroupMatchMap(ArrayList<Challenge> _challenges, ArrayList<ArrayList<Group>> _groups)
    {
        this.Initialize(_challenges, _groups);
    }
    
    private final Map<Challenge, ArrayList<Group>> _map = new HashMap<>();
    
    // This is intended only for initial placements!
    private boolean addToChallenge(Challenge challenge, ArrayList<Group> groups)
    {
        if (this._map.get(challenge) != null) {
            this._map.replace(challenge, groups);
            return true;
        }
        
        return false;
    }
    
    private boolean __performInsideInvestigation()
    {
        // ---------------------------------------------------------------------------------------------
        // --------- This is intended as debug checking reasons, don't let it into production! ---------
        
        for (Map.Entry pair: this._map.entrySet()) 
        {
            ArrayList<Group> gr = (ArrayList<Group>)pair.getValue();
            Challenge ch = (Challenge)pair.getKey();
            
            
            for (Group group: gr)
                if (!group.forWho().equals(ch)) {
                    FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Inside investigation found an intruder, algorithm is not assigning right groups to referenced challenge!");
                    return true;
                }
        }
        
        // ---------------------------------------------------------------------------------------------
        return false;
    }
    
    private int importData(ArrayList<Challenge> _challenges, ArrayList<ArrayList<Group>> _solversGroup)
    {
        int _difference = _challenges.size() - _solversGroup.size();
        
        for (int i = 0; i < _challenges.size(); i++)
            this._map.put(_challenges.get(i), new ArrayList<>());
        
        for (ArrayList<Group> groups: _solversGroup)
            if (!groups.isEmpty())
                if (this.containsChallenge(groups.get(0).forWho()))
                    this._map.replace(groups.get(0).forWho(), groups);
        
        //this.__performInsideInvestigation();
        
        return _difference;
    }
    
    public void Initialize(ArrayList<Challenge> _challenges, ArrayList<ArrayList<Group>> _groups)
    {
        this.importData(_challenges, _groups);
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

    public Group put(Challenge key, Group value) 
    {
        this._map.get(key).add(value);
        return null;
    }
    
    public int ProcessMatch()
    {
        int _amountSolved = 0;
        for (Map.Entry pair: this._map.entrySet()) 
        {
            ArrayList<Group> gr = (ArrayList<Group>)pair.getValue();
            Challenge ch = (Challenge)pair.getKey();
            
            for (int i = 0; i < gr.size(); i++)
            {
                if (!ch.isSolved() && 
                    !gr.get(i).hasSolvedLastChallenge() &&
                    gr.get(i).getSolvedChallenge() == null)
                {
                    if (gr.get(i).canTryAnyway())
                    {
                        if (gr.get(i).attemptSolve())
                        {
                            ch._idledRounds = 0;
                            ch._isGroupSolved = true;
                            
                            for (SolverAgent x: gr.get(i).getMembers()) 
                            {
                                x.getStats()._idledRounds = 0;
                                ch.forceAssignSuccess(x);
                            }
                           
                            _amountSolved++;
                        } else {
                            // They failed to solve.
                        }
                    } else {
                        // Cannot try.
                    }
                } else {
                    // Something was not satisfying the requirements.
                    FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Group Matchmaking requirement check resulted in somebody having active solved flag, therefore we skip all the rest of the groups.");
                }
            }
            
            // Disband is most like declaring the end of the purpose of this group, resetting unneeded parameters and keeping the flags for success; those will
            // be updated on new group formation.
            for (int i = 0; i < gr.size(); i++)
                gr.get(i).disband();
        }
        
        
        return _amountSolved;
    }
    
}