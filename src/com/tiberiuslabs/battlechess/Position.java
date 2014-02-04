package com.tiberiuslabs.battlechess;


public class Position implements Comparable {
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Object o) {
        if (o != null) {
            if (o.getClass() == this.getClass()) {
                if (x == ((Position)o).x) {
                    if (y == ((Position)o).y) {
                        return 0;
                    }
                    else if (y < 0 && ((Position)o).y < 0 ||
                             y >= 0 && ((Position)o).y >= 0) {
                        return Math.abs(y) - Math.abs(((Position)o).y);
                    }
                    else if (y < ((Position)o).y) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
                else {
                    if (x == ((Position)o).x) {
                        return 0;
                    }
                    else if (x < 0 && ((Position)o).x < 0 ||
                            x >= 0 && ((Position)o).x >= 0) {
                        return Math.abs(x) - Math.abs(((Position)o).x);
                    }
                    else if (x < ((Position)o).x) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }

            return this.toString().compareTo(o.toString());
        }

        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (this.getClass() == o.getClass()) {
                return ((Position)o).x == this.x && ((Position)o).y == this.y;
            }
        }
        return false;
    }
}
