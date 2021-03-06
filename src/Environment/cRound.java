/*
 * Decompiled with CFR 0_118.
 */
package Environment;

import AdvStructures.MatchMap;
import AdvStructures.SmartMatchMap;
import Agents.Group.Group;
import Agents.Group.GroupFormer;
import Agents.Group.GroupManager;
import Agents.Properties.cSkill;
import Agents.ProposerAgent;
import Agents.SolverAgent;
import Challenge.Challenge;
import Common.Logging.ILogManager;
import auresearch.FactoryHolder;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class cRound implements IRound 
{
    private String _id = null;
    private Random _random = new Random();
    private int _roundIndex = -1;
    private SecureRandom _srandom = new SecureRandom();
    private boolean _eradicated = false;
    private ArrayList<SolverAgent> _sAgents = new ArrayList();
    private ArrayList<ProposerAgent> _pAgents = new ArrayList();
    private Iterator<ProposerAgent> _pAIter = null;
    private ArrayList<SolverAgent> _deadSAgents = new ArrayList();
    private ArrayList<ProposerAgent> _deadPAgents = new ArrayList();
    private ArrayList<Challenge> _deadChallenges = new ArrayList();
    private ArrayList<Group> _groupPool = new ArrayList();
    private ArrayList<Challenge> _challenge = new ArrayList();
    public roundStatsHolder _stats = new roundStatsHolder();
    private GroupManager _groupHandler = null;
    // WITH THE NEW MATCHMAP ALGORITHM YOU NEED GROUP FORMER ONLY!
    private GroupFormer  _groupFormer = null;
    private SmartMatchMap _smartMatchMap = null;
    private MatchMap _map = new MatchMap();

    public ArrayList<Challenge> getDeadChallenges()
    {
        return this._deadChallenges;
    }
    
    private void _pGenerationChanceStep() 
    {
        int _curChance = this._random.nextInt(100);
        if (_curChance >= FactoryHolder._configManager.getNumberValue("PA_CHANCE_OF_GENERATION")) 
        {
            if (!this._pAgents.isEmpty()) 
            {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "New pagent clone creation:");
                int _randomPick = this._random.nextInt(this._pAgents.size());
                
                if (FactoryHolder._configManager.getStringValue("PA_NOT_A_CLONE").equals("true"))
                    this._pAgents.add(new ProposerAgent());
                else
                    this._pAgents.add(this._pAgents.get(_randomPick).clone());
                
                this._pAgents.get(this._pAgents.size() - 1)._generateProblem();
                this._challenge.add(this._pAgents.get(this._pAgents.size() - 1).getChallengeProposed());
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, this._pAgents.get(this._pAgents.size() - 1).toString());
            } else {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Trying to clone a pagent, but there aren't...");
            }
        }
    }

    public ArrayList<SolverAgent> getDeadSolvers() {
        return this._deadSAgents;
    }

    private void _sGenerationChanceStep() 
    {
        int _curChance = this._random.nextInt(100);
        if (_curChance >= FactoryHolder._configManager.getNumberValue("SA_CHANCE_OF_GENERATION")) 
        {
            if (!this._sAgents.isEmpty()) 
            {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "New sagent clone creation:");
                int _randomPick = this._random.nextInt(this._sAgents.size());
                if (FactoryHolder._configManager.getStringValue("SA_ENABLE_MAX_CLONATION").equals("true")) 
                {
                    if (this._sAgents.get((int)_randomPick).getStats()._clonedTimes <= FactoryHolder._configManager.getNumberValue("SA_MAX_CLONATIONS")) 
                    {
                        if (FactoryHolder._configManager.getStringValue("SA_NOT_A_CLONE").equals("true"))
                            this._sAgents.add(new SolverAgent());
                        else
                            this._sAgents.add(this._sAgents.get(_randomPick).clone());
                    } else {
                        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Max clonations for " + this._sAgents.get(_randomPick) + " reached, skipping.");
                    }
                } else {
                    if (FactoryHolder._configManager.getStringValue("SA_NOT_A_CLONE").equals("true"))
                        this._sAgents.add(new SolverAgent());
                    else
                        this._sAgents.add(this._sAgents.get(_randomPick).clone());
                }
            } else {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Trying to clone a sagent, but there aren't...");
            }
        }
    }

    public cRound(int _round, ArrayList<SolverAgent> _sAPool, ArrayList<ProposerAgent> _pAPool) 
    {
        this._roundIndex = _round;
        this.setID(new BigInteger(130, this._srandom).toString(32));
        this.setRoundIndex(_round);
        this.createRound();
        this._sAgents = _sAPool;
        this._pAgents = _pAPool;
        this._pAIter = this._pAgents.iterator();
        
        this._sAgents.forEach(i -> {
            i.resetForNewRound();
        });
        
        while (this._pAIter.hasNext()) {
            ProposerAgent _tmp = this._pAIter.next();
            _tmp._generateProblem();
            this._challenge.add(_tmp.getChallengeProposed());
        }
    }

    public ArrayList<Challenge> exportChallenges() 
    {
        return this._challenge;
    }

    public cRound(int _round, ArrayList<SolverAgent> _sAPool, ArrayList<ProposerAgent> _pAPool, ArrayList<Challenge> _challenges) 
    {
        Object _tmp;
        int i2;
        this._sAgents = _sAPool;
        this._pAgents = _pAPool;
        this._challenge = _challenges;
        this._sAgents.forEach(i -> {
            i.resetForNewRound();
        });
        // This is mortality rate, it has been disabled because unused and to avoid unwanted influence, but it can be included again if needed.
        /*
        for (i2 = 0; i2 < FactoryHolder._configManager.getNumberValue("MORTALITY_RATE"); ++i2) {
            if (this._random.nextBoolean()) {
                if (this._sAgents.isEmpty()) {
                    this._eradicated = true;
                    continue;
                }
                _tmp = this._sAgents.get(this._random.nextInt(this._sAgents.size()));
                this._deadSAgents.add((SolverAgent)_tmp);
                this._sAgents.remove(_tmp);
                continue;
            }
            if (this._pAgents.isEmpty()) {
                this._eradicated = true;
                continue;
            }
            _tmp = this._pAgents.get(this._random.nextInt(this._pAgents.size()));
            this._deadPAgents.add((ProposerAgent)_tmp);
            this._pAgents.remove(_tmp);
        }
*/
        for (i2 = 0; i2 < this._challenge.size(); ++i2) 
        {
            if (!this._pAgents.contains(this._challenge.get(i2).getAuthor())) 
            {
                _tmp = this._challenge.get(i2);
                this._deadChallenges.add((Challenge)_tmp);
                this._challenge.remove(i2);
                continue;
            }
            if (!this._challenge.get(i2).isSolved()) continue;
            ++this._challenge.get((int)i2).getAuthor()._problemsSolvedAmount;
            this._challenge.get(i2).mutate();
        }
        
        for (i2 = 0; i2 < FactoryHolder._configManager.getNumberValue("PA_EXPONENTIAL_GENERATION_CHANCE"); ++i2)
            this._pGenerationChanceStep();
        
        for (i2 = 0; i2 < FactoryHolder._configManager.getNumberValue("SA_EXPONENTIAL_GENERATION_CHANCE"); ++i2)
            this._sGenerationChanceStep();
            
        this.checkRageQuitters();
        this.checkRageQuitters();
        
        /*
        System.out.println("--------------- " + this._roundIndex + " ---------------");
        for (int i = 0; i < this._deadSAgents.size(); i++)
        {
            if (this._deadSAgents.get(i).getStats()._idledRounds >= 6)
                System.out.println(this._deadSAgents.get(i).getStats()._idledRounds);
        }
        
        System.out.println("--------------- " + this._roundIndex + " ---------------");
        for (int i = 0; i < this._deadPAgents.size(); i++)
        {
            if (this._deadPAgents.get(i).getChallengeProposed()._idledRounds > 5)
                System.out.println(this._deadPAgents.get(i).getChallengeProposed()._idledRounds);
        }
        */
    }

    public cRound(cSkill _type, int _round) {
        this.setID(new BigInteger(130, this._srandom).toString(32));
        this.setRoundIndex(_round);
    }

    public ArrayList<Group> getGroups() {
        return this._groupPool;
    }

    private void checkRageQuitters() 
    {
        int i;
        int _removedAgents = 0;
        for (i = 0; i < this._sAgents.size(); ++i) {
            if (this._sAgents.get((int)i).getStats()._idledRounds < FactoryHolder._configManager.getNumberValue("SA_MAX_IDLED_ROUNDS")) 
                continue;
            this._deadSAgents.add(this._sAgents.get(i));
            this._sAgents.remove(i);
            ++_removedAgents;
        }
        for (i = 0; i < this._challenge.size(); ++i) {
            if (this._challenge.get((int)i)._idledRounds < FactoryHolder._configManager.getNumberValue("CH_MAX_IDLE_ROUNDS")) 
                continue;
            ProposerAgent _reference = this._challenge.get(i).getAuthor();
            this._deadPAgents.add(_reference);
            this._pAgents.remove(_reference);
            this._deadChallenges.add(this._challenge.get(i));
            this._challenge.remove(i);
        }
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Removed " + _removedAgents + " agents for rage quitting.");
    }

    public void run() 
    {
        if (!this._eradicated) 
        {
            if (FactoryHolder._configManager.getStringValue("GAME_TYPE").equals("sorted")) 
            {
                _map.Initialize(_challenge, _sAgents, MatchMap._MATCH_TYPE.TYPE_SORTED);
                
                this._match();
                
            } else if (FactoryHolder._configManager.getStringValue("GAME_TYPE").equals("random")) {
                _map.Initialize(_challenge, _sAgents, MatchMap._MATCH_TYPE.TYPE_RANDOM);
                
                this._match();
                
            } else if (FactoryHolder._configManager.getStringValue("GAME_TYPE").equals("smart_sorted")) {
                this._smartMatchMap = new SmartMatchMap(this._challenge, this._sAgents);
                
                this._match();
                
            } else {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "No valid game type selected, aborting.");
            }
        }
    }

    private void _match() 
    {
        /*
        int i;
        ArrayList _touchedCh = (ArrayList)this._challenge.clone();
        
        ArrayList<SolverAgent> _idled = new ArrayList();
        ArrayList<Challenge> _idledCh = new ArrayList();
        */
        
        // OLD UNSTABLE ALGORITHM, NOT USED ANYMORE, STICK TO MATCHMAPS.
        
        /*
        for (int _trials = 0; !this.getUnsolvedChallenges(this._challenge).isEmpty() && !this.getUnsolvedSAgents(this._sAgents).isEmpty() && _trials < FactoryHolder._configManager.getNumberValue("NUMBER_OF_CHANCES_AN_AGENT_HAS_TO_TRY_TO_FIND_A_PROBLEM"); ++_trials) {
            i = 0;
            int k = 0;
            while (i < this._challenge.size() && k < this._sAgents.size()) {
                if (this._challenge.get(i).isSolved() && this._sAgents.get(k).getHasSolvedLastChallenge()) {
                    i++;
                    ++k;
                    continue;
                }
                if (!this._challenge.get(i).isSolved() && this._sAgents.get(k).getHasSolvedLastChallenge()) {
                    ++k;
                    continue;
                }
                if (this._challenge.get(i).isSolved() && !this._sAgents.get(k).getHasSolvedLastChallenge()) {
                    ++i;
                    continue;
                }
                if (!this._challenge.get(i).isSolved() && !this._sAgents.get(k).getHasSolvedLastChallenge()) {
                    if (_canProceedWithChallenge(this._sAgents.get(k), this._challenge.get(i))) 
                    {
                        if (this._challenge.get(i).attemptSolve(this._sAgents.get(k))) 
                        {
                                this._challenge.get(i)._idledRounds = 0;
                                this._challenge.get(i).forceAssignSuccess(this._sAgents.get(k));
                                this._sAgents.get(k).setSolvedLastChallenge(true);
                                this._sAgents.get(k).getStats()._idledRounds = 0;
                        }
                        this._sAgents.get(k).setTryHarder(this._sAgents.get(k).getTryHarded() + 1);
                        //this._sAgents.get(k).getStats()._idledRounds++;
                        //this._challenge.get(i)._idledRounds++;
                        
                        i++;
                        ++k;
                        continue;
                    } else {
                        k++;
                    }
                }
            }
        }
        
        for (int l = 0; l < this._sAgents.size(); l++)
            if (this._sAgents.get(l).getHasSolvedLastChallenge()) {
                _idled.remove(this._sAgents.get(l));
                this._sAgents.get(l).getStats()._idledRounds = 0;
            } else {
                this._sAgents.get(l).getStats()._idledRounds++;
                _idled.add(this._sAgents.get(l));
            }
        
        for (int o = 0; o < this._challenge.size(); o++)
            if (this._challenge.get(o).isSolved()) {
                _idledCh.remove(this._challenge.get(o));
                this._challenge.get(o)._idledRounds = 0;
            } else {
                this._challenge.get(o)._idledRounds++;
                _idledCh.add(this._challenge.get(o));
            }
        */
        int _iterations = 1;
        while (_iterations <= FactoryHolder._configManager.getNumberValue("SUBROUND_ITERATIONS"))
        {
            if (FactoryHolder._configManager.getStringValue("GAME_TYPE").equals("smart_sorted"))
                this._smartMatchMap.ProcessMatch();
            else
                _map.ProcessMatch();
            
            this.checkRageQuitters();
            _iterations++;
        }
        
        
        if (FactoryHolder._configManager.getStringValue("ENABLE_GROUPS").equals("true")) 
        {
            this._groupFormer = new GroupFormer(this._challenge, this._sAgents);
            
            if (this._groupFormer.isReady())
            {
                if (this._groupFormer.processMatch())
                {
                    FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Success, Groups managed to not be lazy asses.");
                } else {
                    FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "No Groups solved at least a challenge, shame!");
                }
            } else
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Group Former not ready, there was a problem!");
            
        }
        
        this.checkRageQuitters();

    }

    public static boolean _canProceedWithChallenge(SolverAgent _agent, Challenge _skillMap) 
    {
        if (FactoryHolder._configManager.getStringValue("REQUIREMENT_AVERAGE_BASED").equals("true"))
        {
            int _avg = 0;
            int _avgCH = 0;
            for (int k = 0; k < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); ++k) {
                _avg += _agent.getSkills().get(k).getExperience();
                _avgCH += _skillMap.getDifficultyMap()[k];
            }
            
            _avg /= FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size();
            _avgCH /= FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size();
            
            if (_avg >= _avgCH)
            {
                if (_agent.getStats()._idledRounds >= FactoryHolder._configManager.getNumberValue("SA_MAX_IDLED_ROUNDS"))
                    return false;
                
                if (_agent.getStats()._idledRounds <= FactoryHolder._configManager.getNumberValue("SA_MAX_IDLED_ROUNDS") &&
                        _agent.getStats()._idledRounds >= 0)
                    return true;
            }
            
            return false;
        } else {
            
            for (int k = 0; k < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); ++k)
                if (_agent.getSkills().get(k).getExperience() + 5 < _skillMap.getDifficultyMap()[k]) 
                    return false;

            if (_agent.getStats()._idledRounds >= FactoryHolder._configManager.getNumberValue("SA_MAX_IDLED_ROUNDS"))
                return false;

            // REINFORCING RULE!
            if (_agent.getStats()._idledRounds <= FactoryHolder._configManager.getNumberValue("SA_MAX_IDLED_ROUNDS") &&
                    _agent.getStats()._idledRounds >= 0)
                return true;

            return false;
        }
    }

    @Override
    public IRound createRound() {
        return null;
    }

    @Override
    public void setID(String _id) {
        this._id = _id;
    }

    @Override
    public String getID() {
        return this._id;
    }

    @Override
    public int getRoundIndex() {
        return this._roundIndex;
    }

    @Override
    public void setRoundIndex(int _round) {
        this._roundIndex = _round;
    }

    public ArrayList<ProposerAgent> getRemovedPAgents(ArrayList<ProposerAgent> _agents, int _count) {
        if (_count >= FactoryHolder._configManager.getNumberValue("REMOVAL_SOLVER_TRESHOLD") && _agents.size() > 1) {
            for (int i = 0; i < _agents.size(); ++i) {
                if (!_agents.get(i).getLastSolved()) continue;
                ProposerAgent _tmp = _agents.get(i);
                this._deadPAgents.add(_tmp);
                _agents.remove(i);
            }
        }
        return _agents;
    }

    public ArrayList<SolverAgent> getRemovedSAgents(ArrayList<SolverAgent> _agents, int _count) {
        if (_count >= FactoryHolder._configManager.getNumberValue("REMOVAL_SOLVER_TRESHOLD") && _agents.size() > 0) {
            for (int i = 0; i < _agents.size(); ++i) {
                if (_agents.get(i).getHasSolvedLastChallenge() && _agents.get(i).getTryHarded() < FactoryHolder._configManager.getNumberValue("REMOVAL_SOLVER_TRESHOLD")) continue;
                SolverAgent _tmp = _agents.get(i);
                this._deadSAgents.add(_tmp);
                _agents.remove(i);
            }
        }
        return _agents;
    }

    public ArrayList<SolverAgent> getSkilledSAgents(ArrayList<SolverAgent> _sAgents, Challenge _challenge) {
        ArrayList<SolverAgent> _nSAgentsList = new ArrayList<SolverAgent>();
        int _score = 0;
        long _start = 0;
        long _end = 0;
        _start = System.nanoTime();
        for (int i = 0; i < _sAgents.size(); ++i) {
            for (int k = 0; k < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); ++k) {
                if (_sAgents.get(i).getSkills().get(k).getExperience() < _challenge.getDifficultyMap()[k]) continue;
                ++_score;
            }
            if (_score == FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size()) {
                _nSAgentsList.add(_sAgents.get(i));
            }
            _score = 0;
        }
        _end = System.nanoTime();
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "getSkilledSAgents took " + (_end - _start) + " nseconds.");
        return _nSAgentsList;
    }

    public ArrayList<SolverAgent> getUnsolvedSAgents(ArrayList<SolverAgent> _sAgents) {
        ArrayList<SolverAgent> _nSAgentsList = new ArrayList<SolverAgent>();
        for (SolverAgent i : _sAgents) {
            if (i.getHasSolvedLastChallenge()) continue;
            _nSAgentsList.add(i);
        }
        return _nSAgentsList;
    }

    public ArrayList<Challenge> getUnsolvedChallenges(ArrayList<Challenge> _challenges) {
        ArrayList<Challenge> _nChallengeList = new ArrayList<Challenge>();
        for (Challenge i : _challenges) {
            if (i.isSolved()) continue;
            _nChallengeList.add(i);
        }
        return _nChallengeList;
    }

    public boolean checkUnsolvedAgentsPresence(ArrayList<SolverAgent> _sAgents) {
        for (SolverAgent i : _sAgents) {
            if (i.getTryHarded() >= FactoryHolder._configManager.getNumberValue("MAX_TRIALS_BEFORE_KICKOUT") || i.getHasSolvedLastChallenge()) continue;
            return true;
        }
        return false;
    }

    public ArrayList<SolverAgent> getSolvedAgents() {
        ArrayList<SolverAgent> _agents = new ArrayList<SolverAgent>();
        for (SolverAgent i : this._sAgents) {
            if (!i.getHasSolvedLastChallenge()) continue;
            _agents.add(i);
        }
        return _agents;
    }

    public ArrayList<SolverAgent> getSolverAgents() {
        return this._sAgents;
    }

    public ArrayList<ProposerAgent> getProposerAgents() {
        return this._pAgents;
    }

    public void setProposerAgents(ArrayList<ProposerAgent> _pAPool) {
        this._pAgents = (ArrayList)_pAPool.clone();
    }

    public void setSolverAgents(ArrayList<SolverAgent> _sAPool) {
        this._sAgents = (ArrayList)_sAPool.clone();
    }

    public boolean checkUnsolvedPresence(ArrayList<Challenge> _challenges) {
        for (Challenge i : _challenges) {
            if (i.isSolved()) continue;
            return true;
        }
        return false;
    }

    public void setChallenges(ArrayList<Challenge> _challenges) {
        this._challenge = (ArrayList)_challenges.clone();
    }

    public ArrayList<Challenge> getChallanges() {
        return this._challenge;
    }
}

