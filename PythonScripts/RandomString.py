#!/usr/bin/env python
# coding: utf-8

# In[3]:


import random

# Define the set of characters to choose from
charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

# Function to generate a random string of a random length
def generate_random_string():
    # Choose a random length between 1 and 20 (or any range you prefer)
    length = random.randint(1, 20)
    return ''.join(random.choice(charset) for _ in range(length))

# Generate 100 random strings
random_strings = [generate_random_string() for _ in range(100000)]

# Save the generated strings to a file (one string per line)
with open("/Users/lylan/UiO/Thesis/MyPaperFiles/Conferences/EdgeEmuEnergyProfiler/random_strings.txt", "w") as file:
    for s in random_strings:
        file.write(s + "\n")

print("100000 random strings have been written to 'random_strings.txt'.")


# In[ ]:




