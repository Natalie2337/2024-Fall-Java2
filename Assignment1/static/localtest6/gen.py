from math import pi, sin, cos

n = 1000
step = 2 * pi / n
for i in range(n):
    x = i * step
    y = cos(x)
    print(y)
