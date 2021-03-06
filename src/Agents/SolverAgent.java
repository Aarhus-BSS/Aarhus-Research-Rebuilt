/*
 * Decompiled with CFR 0_118.
 */
package Agents;

import Agents.Properties.cSkill;
import Agents.Properties.cStatistics;
import static Agents.SolverAgent.COMPARATOR_SWITCH_TYPE.COMPARATOR_HIGHEST_TO_LOWEST;
import static Agents.SolverAgent.COMPARATOR_SWITCH_TYPE.COMPARATOR_LOWEST_TO_HIGHEST;
import Common.Logging.ILogManager;
import auresearch.FactoryHolder;
import java.util.ArrayList;
import java.util.Random;

public class SolverAgent implements Comparable<SolverAgent> 
{
    public static enum COMPARATOR_SWITCH_TYPE
    {
        COMPARATOR_LOWEST_TO_HIGHEST,
        COMPARATOR_HIGHEST_TO_LOWEST
    }
    
    private ArrayList<cSkill> _skills = new ArrayList();
    private cStatistics _stats = new cStatistics();
    private Random _random = new Random();
    public boolean _solvedLastChallenge = false;
    private int _tryHardedLastChallenge = 0;
    public boolean _isInGroup = false;
    public boolean _solvedLastChallengeAsGroup = false;
    public int _reputationScore = 0;
    public int _failedMeet = 0;
    public COMPARATOR_SWITCH_TYPE _comparatorType = COMPARATOR_HIGHEST_TO_LOWEST;

    public void _setupAgent() 
    {
        int i;
        for (i = 0; i < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); ++i) {
            this._skills.add(new cSkill(FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").get(i).toString()));
        }
        for (i = 0; i < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); ++i) {
            this._skills.get(i).setExperience(this._random.nextInt((FactoryHolder._configManager.getNumberValue("SA_MAXIMUM_EXPERIENCE") -
                                              FactoryHolder._configManager.getNumberValue("SA_MINIMUM_EXPERIENCE")) + 1) + 
                                              FactoryHolder._configManager.getNumberValue("SA_MINIMUM_EXPERIENCE"));
        }
        this._stats._money = 0;
        this._stats._successTrials = 0;
        this._stats._trials = 0;
        this._stats._rejected = 0;
        this._isInGroup = false;
        this._reputationScore = this._random.nextInt(100);
        
        if (FactoryHolder._configManager.getStringValue("SA_SORTED_RANK_TYPE").equals("HTL"))
            this._comparatorType = COMPARATOR_HIGHEST_TO_LOWEST;
        else if (FactoryHolder._configManager.getStringValue("SA_SORTED_RANK_TYPE").equals("LTH"))
            this._comparatorType = COMPARATOR_LOWEST_TO_HIGHEST;
        
    }

    public void _setupAgent(ArrayList<cSkill> _skills) 
    {
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Creating new Agent (prebuilt skills)");
        this._stats._money = 0;
        this._stats._successTrials = 0;
        this._stats._trials = 0;
        this._stats._rejected = 0;
        this._isInGroup = false;
        this._skills = (ArrayList)_skills.clone();
        this._reputationScore = this._random.nextInt(100);
        
        
        if (FactoryHolder._configManager.getStringValue("SA_SORTED_RANK_TYPE").equals("HTL"))
            this._comparatorType = COMPARATOR_HIGHEST_TO_LOWEST;
        else if (FactoryHolder._configManager.getStringValue("SA_SORTED_RANK_TYPE").equals("LTH"))
            this._comparatorType = COMPARATOR_LOWEST_TO_HIGHEST;
    }

    public cStatistics getStats() {
        return this._stats;
    }

    public void resetForNewRound() {
        this._solvedLastChallenge = false;
        this._tryHardedLastChallenge = 0;
        this._isInGroup = false;
        this._solvedLastChallengeAsGroup = false;
    }

    public void setSolvedLastChallenge(boolean _status) {
        this._solvedLastChallenge = _status;
        this._stats._idledRounds = 0;
    }

    public void setTryHarder(int _status) {
        this._tryHardedLastChallenge = _status;
        this._stats._idledRounds++;
    }

    public boolean getHasSolvedLastChallenge() {
        return this._solvedLastChallenge;
    }

    public int getTryHarded() {
        return this._tryHardedLastChallenge;
    }

    public SolverAgent() {
        this._setupAgent();
    }

    public SolverAgent(ArrayList<cSkill> _skills) {
        this._setupAgent(_skills);
    }

    public void setComparatorSwitch(COMPARATOR_SWITCH_TYPE _type)
    {
        this._comparatorType = _type;
    }
    
    @Override
    public int compareTo(SolverAgent _agent) 
    {
        switch (this._comparatorType)
        {
            case COMPARATOR_LOWEST_TO_HIGHEST:
                return this.getTotalExperience() - _agent.getTotalExperience() ;
            case COMPARATOR_HIGHEST_TO_LOWEST:
                return _agent.getTotalExperience() - this.getTotalExperience();
        }
        return -1;
    }

    public void addExpToSkill(String _skillName, int _experienceAmount) {
        for (int i = 0; i < this._skills.size(); ++i) {
            if (!this._skills.get(i).getName().equals(_skillName)) continue;
            this._skills.get(i).addExperience(_experienceAmount);
            return;
        }
    }

    public boolean isCompetent(String _skillName) {
        for (int i = 0; i < this._skills.size(); ++i) {
            if (!this._skills.get(i).getName().equals(_skillName) || this._skills.get(i).getExperience() < FactoryHolder._configManager.getNumberValue("SA_MINIMAL_COMPETENCY_EXPERIENCE")) continue;
            return true;
        }
        return false;
    }

    public void giveReward(int _amount) {
        this._stats._money += _amount;
    }
    
    public int[] getSkillsArray()
    {
        int _agentMap[] = new int[this._skills.size()];
        for (int k = 0; k < this._skills.size(); k++)
                _agentMap[k] = this._skills.get(k).getExperience();
        
        return _agentMap;
    }

    public cSkill getSkill(String _skillName) {
        for (int i = 0; i < this._skills.size(); ++i) {
            if (!this._skills.get(i).getName().equals(_skillName)) continue;
            return this._skills.get(i);
        }
        return null;
    }

    public int getTotalExperience() {
        int _total = 0;
        for (int i = 0; i < this._skills.size(); ++i) {
            _total += this._skills.get(i).getExperience();
        }
        return _total;
    }

    private ArrayList<cSkill> _mutatePositiveSkills() 
    {
        ArrayList _mutatedSkills = (ArrayList)this._skills.clone();
        int _outExp = 0;
        int _curExp = 0;
        for (int i = 0; i < this._skills.size(); ++i) 
        {
            _curExp = this._skills.get(i).getExperience();
            _outExp = _curExp + FactoryHolder._configManager.getNumberValue("SA_MUTATION_RATE_VALUE") * _curExp / 100;
            ((cSkill)_mutatedSkills.get(i)).setExperience(_outExp);
        }
        return _mutatedSkills;
    }

    private cSkill _mutateSkill(cSkill _oldSkill, String _rateoSign)
    {
        cSkill _newSkill = _oldSkill.clone();
        int _outExp = 0;
        int _curExp = _oldSkill.getExperience();
        int _rate = this._random.nextInt(FactoryHolder._configManager.getNumberValue("SA_MUTATION_RATE_VALUE") + 1);
        if (_rateoSign.equals("+/-")) {
            boolean _sign = this._random.nextBoolean();
       
            _outExp = _sign ? _curExp + _rate * _curExp / 100 : _curExp - FactoryHolder._configManager.getNumberValue("SA_MUTATION_RATE_VALUE") * _curExp / 100;
        } else if (_rateoSign.equals("+")) {
            _outExp = _curExp + _rate * _curExp / 100;
        } else if (_rateoSign.equals("-")) {
            _outExp = _curExp - _rate * _curExp / 100;
        } else {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Rateo sign is not recognized.");
        }
        _newSkill.setExperience(_outExp);
        return _newSkill;
    }

    public void addExperience(int _amount) {
        this._stats._generalExperience += _amount;
    }

    private ArrayList<cSkill> _mutateNegativeSkills() {
        int _factor = this._random.nextInt(FactoryHolder._configManager.getNumberValue("SA_MUTATION_RATE_VALUE"));
        ArrayList _mutatedSkills = (ArrayList)this._skills.clone();
        for (int i = 0; i < this._skills.size(); ++i) {
            ((cSkill)_mutatedSkills.get(i)).setExperience((this._skills.get(i).getExperience() - _factor) * (this._skills.get(i).getExperience() / 100));
        }
        return _mutatedSkills;
    }

    public String compositeExperience() {
        double _comp = 0.0;
        for (int i = 0; i < this._skills.size(); ++i) {
            _comp += (double)this._skills.get(i).getExperience();
        }
        return String.valueOf(_comp / (double)this._skills.size());
    }

    public ArrayList<cSkill> getSkills() {
        return this._skills;
    }
    
    public void mutateReputation(int _original)
    {
        if (FactoryHolder._configManager.getStringValue("ENABLE_REPUTATION").equals("true"))
            if (FactoryHolder._configManager.getStringValue("SA_ENABLE_MUTATION").equals("true"))
            {
                int _rate = this._random.nextInt(FactoryHolder._configManager.getNumberValue("SA_MUTATION_RATE") + 1);
                if (FactoryHolder._configManager.getStringValue("SA_MUTATION_SIGN").equals("+/-")) {

                    boolean _throw = this._random.nextBoolean();
                    if (_throw)
                        this._reputationScore = (_original + _rate) * (_original / 100);
                    else
                        this._reputationScore = (_original - _rate) * (_original / 100);

                } else if (FactoryHolder._configManager.getStringValue("SA_MUTATION_SIGN").equals("-")) {
                    this._reputationScore = (_original - _rate) * (_original / 100);
                } else if (FactoryHolder._configManager.getStringValue("SA_MUTATION_SIGN").equals("+")) {
                    this._reputationScore = (_original + _rate) * (_original / 100);
                } else 
                    FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Mutation sign for challenge is unrecognized.");
            }
    }

    public SolverAgent clone() {
        ArrayList<cSkill> _newSet = new ArrayList<cSkill>();
        cSkill _newSkillSlot = null;
        SolverAgent _sa = null;
        ++this._stats._clonedTimes;
        if (FactoryHolder._configManager.getStringValue("SA_ENABLE_MUTATION_RATE").equals("true")) 
        {
            for (int i = 0; i < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); ++i) {
                _newSkillSlot = this._mutateSkill(this._skills.get(i), FactoryHolder._configManager.getStringValue("SA_MUTATION_RATE_SIGN"));
                _newSet.add(_newSkillSlot);
            }
            _sa = new SolverAgent(_newSet);
            _sa.mutateReputation(this._reputationScore);
            return _sa;
        }
        _sa = new SolverAgent(this._skills);
        _sa.mutateReputation(this._reputationScore);
        return _sa;
    }

    public void _incrementRejected() {
        ++this._stats._rejected;
        ++this._tryHardedLastChallenge;
    }
}

