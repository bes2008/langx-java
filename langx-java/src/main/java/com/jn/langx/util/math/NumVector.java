package com.jn.langx.util.math;

public class NumVector {
    private final double[] array;

    public static NumVector create(double... values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("no values");
        } else {
            return new NumVector(values.clone());
        }
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else {
            if (object instanceof NumVector) {
                NumVector vector = (NumVector)object;
                int size = vector.getSize();
                if (size == this.getSize()) {
                    for(int i = 0; i < size; ++i) {
                        if (Double.doubleToLongBits(this.get(i)) != Double.doubleToLongBits(vector.get(i))) {
                            return false;
                        }
                    }

                    return true;
                }
            }

            return false;
        }
    }

    public String toString() {
        int size = this.getSize();
        StringBuilder sb = new StringBuilder();
        sb.append("Vector ").append(size).append(" { ");

        for(int i = 0; i < size; ++i) {
            if (i != 0) {
                sb.append(", ");
            }

            sb.append(this.get(i));
        }

        return sb.append(" }").toString();
    }

    public double get(int index) {
        return this.array[index];
    }

    public int getSize() {
        return this.array.length;
    }

    public NumVector plus(NumVector vector) {
        int size = vector.getSize();
        if (size != this.getSize()) {
            throw new IllegalArgumentException("sizes mismatch");
        } else {
            double[] result = this.toArray();

            for(int i = 0; i < result.length; ++i) {
                result[i] += vector.get(i);
            }

            return new NumVector(result);
        }
    }

    public NumVector minus(NumVector vector) {
        int size = vector.getSize();
        if (size != this.getSize()) {
            throw new IllegalArgumentException("sizes mismatch");
        } else {
            double[] result = this.toArray();

            for(int i = 0; i < result.length; ++i) {
                result[i] -= vector.get(i);
            }

            return new NumVector(result);
        }
    }

    public NumVector multiply(double value) {
        double[] result = this.toArray();

        for(int i = 0; i < result.length; ++i) {
            result[i] *= value;
        }

        return new NumVector(result);
    }

    public NumVector multiply(Matrix matrix) {
        int size = matrix.getRows();
        if (size != this.getSize()) {
            throw new IllegalArgumentException("size mismatch rows");
        } else {
            double[] result = new double[matrix.getColumns()];

            for(int i = 0; i < result.length; ++i) {
                result[i] = this.multiply(matrix.getColumn(i));
            }

            return new NumVector(result);
        }
    }

    public double multiply(NumVector vector) {
        int size = vector.getSize();
        if (size != this.getSize()) {
            throw new IllegalArgumentException("sizes mismatch");
        } else {
            double result = 0.0D;

            for(int i = 0; i < size; ++i) {
                result += this.get(i) * vector.get(i);
            }

            return result;
        }
    }

    public double length() {
        return Math.sqrt(this.multiply(this));
    }

    NumVector(double[] array) {
        this.array = array;
    }

    double[] toArray() {
        return this.array.clone();
    }

    static class Modified extends NumVector {
        Modified(NumVector vector) {
            super(vector.array);
        }

        @Override
        double[] toArray() {
            int size = this.getSize();
            double[] result = new double[size];

            for(int i = 0; i < result.length; ++i) {
                result[i] = this.get(i);
            }

            return result;
        }
    }
}
