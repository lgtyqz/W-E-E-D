from tkinter import *
import math
import random
root = Tk()
superseed = "Don't catch you slippin'"
seed = hash(superseed)
random.seed(superseed)
def perlin_noise(x, y, z):
    X = int(x) & 255                  # FIND UNIT CUBE THAT
    Y = int(y) & 255                  # CONTAINS POINT.
    Z = int(z) & 255
    x -= int(x)                                # FIND RELATIVE X,Y,Z
    y -= int(y)                                # OF POINT IN CUBE.
    z -= int(z)
    u = fade(x)                                # COMPUTE FADE CURVES
    v = fade(y)                                # FOR EACH OF X,Y,Z.
    w = fade(z)
    A = p[X  ]+Y; AA = p[A]+Z; AB = p[A+1]+Z      # HASH COORDINATES OF
    B = p[X+1]+Y; BA = p[B]+Z; BB = p[B+1]+Z      # THE 8 CUBE CORNERS,
 
    return lerp(w, lerp(v, lerp(u, grad(p[AA  ], x  , y  , z   ),  # AND ADD
                                   grad(p[BA  ], x-1, y  , z   )), # BLENDED
                           lerp(u, grad(p[AB  ], x  , y-1, z   ),  # RESULTS
                                   grad(p[BB  ], x-1, y-1, z   ))),# FROM  8
                   lerp(v, lerp(u, grad(p[AA+1], x  , y  , z-1 ),  # CORNERS
                                   grad(p[BA+1], x-1, y  , z-1 )), # OF CUBE
                           lerp(u, grad(p[AB+1], x  , y-1, z-1 ),
                                   grad(p[BB+1], x-1, y-1, z-1 ))))
 
def fade(t): 
    return t ** 3 * (t * (t * 6 - 15) + 10)
 
def lerp(t, a, b):
    return a + t * (b - a)
 
def grad(hash, x, y, z):
    h = hash & 15                      # CONVERT LO 4 BITS OF HASH CODE
    u = x if h<8 else y                # INTO 12 GRADIENT DIRECTIONS.
    v = y if h<4 else (x if h in (12, 14) else z)
    return (u if (h&1) == 0 else -u) + (v if (h&2) == 0 else -v)

def toHex(colorList):
    return "#" + \
                    hex(colorList[0])[2:].zfill(2) + \
                    hex(colorList[1])[2:].zfill(2) + \
                    hex(colorList[2])[2:].zfill(2)
 
p = [None] * 512
permutation = [151,160,137,91,90,15,
   131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
   190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
   88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
   77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
   102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
   135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
   5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
   223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
   129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
   251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
   49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
   138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180]
for i in range(256):
    p[256+i] = p[i] = permutation[i]

def pRNGsus(seed, j, i):
    return int(seed * (j ^ i)) ^ 255

active = False
disp = Canvas(root, width=400, height=400, bg="white")
disp.grid(row=0, column=0, columnspan=4)
##for i in range(50):
##    print(pRNGsus(seed, 100, i))
for i in range(0, 100): 
    for j in range(0, 100):
        result = min(int((perlin_noise(j/32, i/32, seed) * 128 + 128)),
                     255)
        enemyDeterminator = pRNGsus(seed, j, i)
        if result > 140 or (60 > i > 40 and 40 < j < 60): # Not part of biome
            colorList = [255, 255, 255]
        elif result > 100: # Otherwise, part of biome - CANNOT SPAWN HERE
            if result % 10 == 0:
                # Weeds
                colorList = [128, 64, 0]
            else:
                # Border enemies
                r = random.random()
                if r < 0.015:
                    colorList = [255, 0, 0]
                elif r < 0.02:
                    colorList = [0, 255, 0]
                else:
                    colorList = [255, 255, 255]
        elif result > 70:
            if result % 6 == 0:
                # Weeds
                colorList = [128, 64, 0]
                if result % 4 == 0:
                    # Stones
                    colorList = [100, 100, 100]
            else:
                # Border enemies
                r = random.random()
                if r < 0.01:
                    colorList = [255, 0, 0]
                elif r < 0.02:
                    colorList = [0, 255, 0]
                elif r < 0.035:
                    # Powerup
                    colorList = [0, 255, 255]
                else:
                    colorList = [255, 255, 255]
        else:
            # Rewards! and hard enemies
            r = random.random()
            if r < 0.05:
                # Shovel
                colorList = [0, 255, 255]
            elif r < 0.06:
                # Bomb
                colorList = [0, 0, 0]
            elif r < 0.063:
                # Hard Enemy
                colorList = [0, 0, 128]
            else:
                colorList = [255, 200, 255]
        #print(colorList)
        disp.create_rectangle(j * 4, i * 4, j * 4 + 4, i * 4 + 4,
                                      fill=toHex(colorList), outline="#AAAAAA")
disp.create_rectangle(200, 200, 204, 204,
        fill="blue", outline="")
root.mainloop()
