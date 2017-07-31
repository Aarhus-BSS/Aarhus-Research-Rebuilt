

library(readxl)
library(tidyverse)
library(stringr)
library(scales)
library(cowplot)


alpha_level = .2
data_folder = "D:\\simulation_results\\clean_output\\"
plot_which_type = "smartsorted"
plot_agent_chances = 3
plot_agent_prob = 33

read_agent_report <- function(filename) {
    read_excel(filename) %>%
        mutate(Agent = as.numeric(as.factor(Agent)),
            Round = as.integer(Round - 1),
            filename = filename)
}

read_problem_report <- function(filename) {
    read_excel(filename) %>%
        mutate(Problem = as.numeric(as.factor(Problem)),
            Round = as.integer(Round),
            filename = filename,
            `Solved` = 1 - `Solved`)
}

read_one_run <- function(agent_file, problem_file, path = test_path) {
    solvers = read_agent_report(str_c(path, agent_file))
    proposers = read_problem_report(str_c(path, problem_file))
    
    return(list(solvers, proposers))
}

plot_agents_once <- function(agent_file, problem_file, save=FALSE) {
    fil = read_one_run(agent_file, problem_file)
    solvers = fil[[1]] %>%
        group_by(Round) %>%
        summarise(n = length(unique(Agent))) %>%
        mutate(who = "Solvers")
    
    proposers = fil[[2]] %>%
        group_by(Round) %>%
        summarise(n = length(unique(Problem))) %>%
        mutate(who = "Proposers")

    number_of_agents = bind_rows(solvers, proposers) %>%
        mutate(who = factor(who, levels = c("Solvers", "Proposers")))

    plo = ggplot(number_of_agents, aes(Round, n, colour = who)) +
        geom_line() +
        geom_point() +
        labs(y = "Number of agents",
             title = "Population") +
        guides(colour = guide_legend(title = element_blank()),
            fill = "none") +
        theme(legend.position = "bottom") +
        scale_x_continuous(breaks = pretty_breaks()) +
        scale_y_continuous(breaks = pretty_breaks(),limits = c(0,NA))

    if (save) {
        ggsave(str_c(plot_path, agent_file, "_agents.png"), plo)
    } else {
        return(plo)
    }
}

plot_skill_once <- function(agent_file, problem_file, save = FALSE) {
    fil = read_one_run(agent_file, problem_file)
    solvers = fil[[1]]
    proposers = fil[[2]]
    
    skill = solvers %>%
        group_by(Round) %>%
        mutate(skill = as.numeric(`Composite Exp`),
            who = "Solvers") %>%
        bind_rows(group_by(proposers, Round) %>%
            mutate(skill = as.numeric(Difficulty) / 4,
            who = "Proposers")) %>%
        mutate(who = factor(who, levels = c("Solvers", "Proposers")))
    
    plo <- ggplot(skill, aes(Round, skill, colour = who, fill = who)) +
        geom_line(stat="summary", fun.y=mean) +
        geom_ribbon(stat="summary", fun.data = mean_se, alpha = .3, colour = NA) +
        labs(y = "Skill / Difficulty",
             title = "Population skill / difficulty") +
        guides(colour = guide_legend(title = element_blank()),
            fill = "none") +
        theme(legend.position = "bottom") +
        scale_x_continuous(breaks = pretty_breaks()) +
        scale_y_continuous(breaks = pretty_breaks(),limits = c(0,NA))
    
    if (save) {
        ggsave(str_c(plot_path, agent_file, "_skill.png"), plo)
    } else {
        return(plo)
    }
    
    
}

plot_success_once <- function(agent_file, problem_file, save=FALSE) {
    fil = read_one_run(agent_file, problem_file)
    solvers = fil[[1]]
    proposers = fil[[2]]
    
    success <- solvers %>%
        group_by(Round) %>%
        summarise(success = mean(`Has Solved`)) %>%
        mutate(who = "Solvers") %>%
        bind_rows(group_by(proposers, Round) %>%
                summarise(success = mean(Solved)) %>%
                mutate(who = "Proposers")) %>%
        mutate(who = factor(who, levels = c("Solvers", "Proposers")))
    
    # print(str(success))
    
    plo <- ggplot(success, aes(Round, success, colour = who)) +
        geom_line() +
        labs(y = "Fraction of success",
             title = "Successful agents") +
        guides(colour = guide_legend(title = element_blank()),
            fill = "none") +
        theme(legend.position = "bottom") +
        scale_x_continuous(breaks = pretty_breaks()) +
        scale_y_continuous(labels = percent,limits = c(0,1))
    
    if (save) {
        ggsave(str_c(plot_path, agent_file, "_success.png"), plo)
    } else {
        return(plo)
    }
}


test_path = "D:\\simulation_results\\clean_output\\random_sigmoid_2_2_60_60_33_3\\Data\\"
ts1 = "AgentReport.29-07-2017-15.11-.-31566029.xls"
ts2 = "ProblemsReport.29-07-2017-15.11-.-91121016.xls"
plot_agents_once(ts1, ts2)
plot_skill_once(ts1, ts2)
plot_success_once(ts1, ts2)




