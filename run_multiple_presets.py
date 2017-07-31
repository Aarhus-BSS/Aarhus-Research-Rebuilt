# -*- coding: utf-8 -*-
import os
from subprocess import call, TimeoutExpired
from functools import reduce
import itertools

"""
Wrapper for calling the AarhusBSS-Research
    collaborative communities simulation script
Author: Malte Lau Petersen, maltelau@protonmail.com

Edit options, folders, and combinations below and run the script
"""


# Should we run the simulations or just print the preset options?
RUN_SIMULATIONS = True
# Should we run the simulations
# even if we find some data in the output folder already?
APPEND_SIMULATIONS = False
# Sometimes the simulations hang. How long (seconds) should we wait for a preset?
TIMEOUT = 100  
# Which folder should we write the output data to?
OUTPUT_FOLDER = "D:\\simulation_results\\clean_output"
# Which combinations should we run?
COMBINATIONS = [[{'sigmoid': 'sigmoid'}, {'sigmoid': 'dicethrow'}],
                [{'type': 'random'}, {'type': 'sorted'}],
                [{'sa_mutate': 2, 'ch_mutate':2}, {'sa_mutate': 50, 'ch_mutate':50}],
                [{'ch_min': 1, 'ch_max': 100}, {'ch_min': 40, 'ch_max': 60}, {'ch_min': 60, 'ch_max': 80}],
                [{'sa_min': 1, 'sa_max': 100}, {'sa_min': 40, 'sa_max': 60}]]

                 
BASE_SETTINGS = {'sigmoid': 'dicethrow',
                 'type': 'random',
                 'sa_mutate': 5,
                 'ch_mutate': 2,
                 'group': "false",
                 'ch_min': 1,
                 'ch_max': 100,
                 'sa_min': 1,
                 'sa_max': 100,
                 'n_skill': 4,
                 'n_reqs': 4,
                 'agent_generation_probability': 33,
                 'agent_generation_chances': 3,
                 'OUTPUT_FOLDER': OUTPUT_FOLDER}

                 
                 
                 
                 
                 
#### You shouldn't need to edit anything below this



                 
                 

# Which skills to use. The names don't matter I think
SKILLS = 'chemistry,biology,mathematics,physics,medicine,economics,language,management,business,it'.split(',')
wd = os.getcwd()


base = """
; ======== Configuration Scenario Settings ========

; Will appear on GUI list.
SCENARIO_NAME="{name}"

; For compatibility checks.
SCENARIO_VERSION=4

; =================================================


; ======== Internal parameters ========

; library: GUI classic view.
; console: Multi-Master Running for big data generation and analysis, this is using multithreading!
;		   NMasterRuns / 5 (number of RoundManager for each thread) = Threads number
;MODE=library
MODE=console

MASTER_ITERATIONS=20

;MERGING_ALPHA=0.3

REPORT_OUTPUT_FOLDER={OUTPUT_FOLDER}\\{name}\\Data
GRAPH_OUTPUT_FOLDER={OUTPUT_FOLDER}\\{name}\\Graphs
PRESET_INPUT_FOLDER=..\\Presets\\

; =====================================

; ========= Environmental parameters ==========

NUMBER_OF_CHANCES_AN_AGENT_HAS_TO_TRY_TO_FIND_A_PROBLEM=3

NUMBER_OF_TRIALS_FOR_SINGLE_AGENT_SOLVING=55

NUMBER_OF_CHANCES_A_GROUP_HAS_TO_TRY_TO_FIND_A_PROBLEM=3

MORTALITY_RATE=0

; =============================================

; ========= Agents parameters =========

;AGENT_SKILLS=chemistry,biology,mathematics,physics,medicine,economics,language,management,business,it
AGENT_SKILLS={skills}

; =====================================

; ========= Challenge parameters =========

REQUIREMENT_AVERAGE_BASED=false

CH_ENABLE_MUTATION=true

CH_MUTATION_SIGN=+/-

CH_MUTATION_RATE={ch_mutate}

CH_DEADLINE=1.0

CH_EASYREJECTOR=false

CH_MINIMAL_DIFFERENCE=1

ENABLE_MAX_IDLED_ROUNDS_CHALLENGE=true

CH_MAX_IDLE_ROUNDS=5

;CH_MIN_REWARD=1

;CH_MAX_REWARD=200

; How to rank the problems in the sorted method
;	- HTL : Highest To Lowest (difficulty)
;	- LTH : Lowest To Highest (difficulty)
CH_SORTED_RANK_TYPE=HTL

; ========================================

; ======== Solver Agent configurations ========

; Minimum experience.
SA_MINIMUM_EXPERIENCE={sa_min}

; Maximum randomized experience for each skill.
SA_MAXIMUM_EXPERIENCE={sa_max}

SA_ENABLE_MUTATION_RATE=true

SA_MUTATION_RATE_VALUE={sa_mutate}

; Mutation rate sign:
;	- +/-
;	- +
;	- -
SA_MUTATION_RATE_SIGN=+/-

SA_MAX_IDLED_ROUNDS=5

; How to rank the agents in the sorted method
;	- HTL : Highest To Lowest (skill)
;	- LTH : Lowest To Highest (skill)
SA_SORTED_RANK_TYPE=HTL

; =============================================

; ======== Clonation parameters ========

SA_EXPONENTIAL_GENERATION_CHANCE={agent_generation_chances}

PA_EXPONENTIAL_GENERATION_CHANCE={agent_generation_chances}

SA_CHANCE_OF_GENERATION={agent_generation_probability}

PA_CHANCE_OF_GENERATION={agent_generation_probability}

SA_ENABLE_MAX_CLONATION=false

SA_NOT_A_CLONE=false

SA_MAX_CLONATIONS=3

; ======================================

; ======== Proposer Agent parameters ========

; Maximum of randomized requirements experience for the challenge.
MAXIMUM_RANDOM_REQUIREMENTS_SELECTED={n_reqs}

MINIMUM_RANDOM_EXPERIENCE_PER_REQUIREMENT={ch_min}

; Max amount of exp for each skill inside a challenge.
MAXIMUM_RANDOM_EXPERIENCE_PER_REQUIREMENT={ch_max}

PA_NOT_A_CLONE=false

; Mutation rate sign:
;	- +/-
;	- +
;	- -
PA_MUTATION_RATE_SIGN=+/-

; ===========================================

; ======== Group parameters ========

ENABLE_GROUPS={group}

USE_SIGMOID=true

MAX_GROUP_MEMBERS=4

SINGULAR_SKILL_SELECTION=true

ALLOW_UNCOVERED_REQUIREMENTS_TRIAL=true

MINIMUM_MEMBERS_TO_ATTEMPT_SOLVE=2

LIMIT_GROUPS_COUNT=true

MAXIMUM_EXISTING_GROUPS=30

ENABLE_INDIVIDUAL_SKILL_COVERAGE=true

; Available models:
;	- 1A
;	- 1B - NA
;	- 1A_WR <- "With Reputation" - NA
;	- 1B_WR - NA
GROUP_MODEL=1A

; ==================================

; ======= REPUTATION ==============

ENABLE_REPUTATION=false

; =================================

; ======== Plotting parameters ===

MINIMAL_RANGE=0

MAXIMAL_RANGE=100

; =================================

; ======== Game parameters ========

; How many runs the same agents effectively do in the same round.
; 1 is considered default.
SUBROUND_ITERATIONS=1

; How many rounds do we have.
MAX_ROUNDS=200

; Deadline limit in minutes.
DEADLINE=1.0

; How many proposer agents do we have?
PROPOSER_AGENTS_AMOUNT=85

; How many solver agents do we have?
SOLVER_AGENTS_AMOUNT=100

; "Game" Type selected, existing are:
;	- random: SAgent picks a random problem and try to solve it.
;	- sorted: SAgent picks a problem by a sorted list.
;	- smart_sorted: SAgent will try to solve all the problems until success.
GAME_TYPE={type}

; Individual SolverAgent will use this method to try this challenge.
; 	- dicethrow (old one)
;	- sigmoid
ATTEMPT_TYPE={sigmoid}

; =================================
"""




if not os.path.exists(OUTPUT_FOLDER) and RUN_SIMULATIONS:
    os.makedirs(OUTPUT_FOLDER)


timeouts = 0

for run_settings in itertools.product(*COMBINATIONS):    
    run_settings = reduce((lambda x,y: {**x, **y}), run_settings)

    params = {**BASE_SETTINGS, **run_settings}
    name = params['type']
    if params['type'] in ["smart_sorted"]:
        name = "smartsorted"
        
    
    params['skills'] = ','.join(SKILLS[:params['n_skill']])

    name = '{type}_{sigmoid}_{sa_mutate}_{ch_mutate}_{sa_max}_{ch_max}_{agent_generation_probability}_{agent_generation_chances}'.format(**params)

    params['name'] = name
    print("[+] Preset: " + name)

    
    if not RUN_SIMULATIONS or \
        (os.path.exists(os.path.join(OUTPUT_FOLDER, name)) and \
             not APPEND_SIMULATIONS):
        print("[-] .. skipped")
        continue
        
    
    if not os.path.exists(os.path.join(OUTPUT_FOLDER, name)):
        os.makedirs(os.path.join(OUTPUT_FOLDER, name))
        os.makedirs(os.path.join(OUTPUT_FOLDER, name, "Data"))
        os.makedirs(os.path.join(OUTPUT_FOLDER, name, "Graphs"))
        
    
    if RUN_SIMULATIONS:
        # write to the actual options file
        with open(os.path.join(wd, "options.cfg"), "w") as f:
            f.write(base.format(**params))
        # call the program
        # java -Xmx 1536m -jar AUResearch-Exp.jar
        try: 
            call(["java", 
                  "-Xmx1536m", 
                  "-jar",
                  os.path.join(wd, "dist", "AUResearch-Exp.jar")],  timeout=TIMEOUT)
            
        except TimeoutExpired:
            print("[+] Timeout expired " + name)
            timeouts += 1
                                        
                                       
print("Timeouts: " + str(timeouts))
                                    
                
                
                
                
                
                
                
                
                
