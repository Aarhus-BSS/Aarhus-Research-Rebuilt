
library(tidyverse)
library(stringr)

data_folder = "D:\\simulation_results\\clean_output"


files <- list.files(data_folder, recursive = TRUE, full.names = TRUE)

for (f in files) {
    if (file.size(f) < 1000 & 
            str_detect(f, "AgentReport|ProblemsReport")) {
        print(f)
        file.remove(f)
    }
}
