/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents.Group;

import AdvStructures.GroupMatchMap;
import Agents.SolverAgent;
import Challenge.Challenge;
import auresearch.FactoryHolder;
import java.util.ArrayList;

/**
 *
 * @author d3vil401
 */
public class GroupFormer 
{
    private ArrayList<ArrayList<Group>> _groupsArray = new ArrayList<>();
    private ArrayList<Group> _deadGroups = new ArrayList();
    private int _saturationScore = 0;
    private ArrayList<Challenge> _challenges = new ArrayList<>();
    private ArrayList<Group> _formation = new ArrayList<>();
    private GroupMatchMap _map = null;
    private boolean _isReady = false;
    
    private int _getSaturationLimit(ArrayList<SolverAgent> _solvers)
    {
        return _solvers.size() / 2;
    }
    
    public int getCHIndexReference(Challenge _ch)
    {
        int _ctr = 0;
        if (this._challenges.contains(_ch))
            for (Challenge c: this._challenges)
            {
                _ctr++;
                if (c.equals(_ch))
                    return _ctr;
            }
        return -1;
    }
    
    public int getGroupsCount(Challenge _ch)
    {
        int _reference = this.getCHIndexReference(_ch);
        if (_reference != -1)
            return this._groupsArray.get(_reference).size();
        
        return 0;
    }
    
    private boolean _saturedPool()
    {
        if (FactoryHolder._configManager.getStringValue("LIMIT_GROUPS_COUNT").equals("true"))
            if (_formation.size() >= FactoryHolder._configManager.getNumberValue("MAXIMUM_EXISTING_GROUPS"))
                return true;
        
        return false;
    }
    
    public GroupFormer(ArrayList<Challenge> _challenges, ArrayList<SolverAgent> _solvers)
    {
        _solvers.forEach((i) -> {
            i.resetForNewRound();
            this._saturationScore = 0;
        });
        
        for (int _challengeIndex = 0; _challengeIndex < _challenges.size(); _challengeIndex++) 
        {
            
            if (!_challenges.get(_challengeIndex).isSolved())
            {
                while (!this._saturedPool())
                {
                    _formation.add(new Group(_solvers, _challenges.get(_challengeIndex), _MODEL_SETUP.fromString(FactoryHolder._configManager.getStringValue("GROUP_MODEL"))));
                    
                    if (_formation.get(_formation.size() - 1)._satured()) {
                        this._saturationScore++;
                        _formation.remove(_formation.size() - 1);
                    }
                    
                    if (this._saturationScore >= this._getSaturationLimit(_solvers))
                        break;
                }
                
                this._groupsArray.add(_formation);
                _formation.clear();
            }
        }
        
        if (this._groupsArray.size() > 0)
            this._map = new GroupMatchMap(this._challenges, this._groupsArray);
        
        this._isReady = true;
    }
    
    public boolean processMatch()
    {
        if (this.isReady())
            if (this._map.ProcessMatch() > 0)
                return true;
        
        return false;
    }
    
    public boolean isReady()
    {
        return this._isReady;
    }
}
