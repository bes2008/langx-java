package com.jn.langx.util.math;
import com.jn.langx.util.math.NumVector.Modified;

public final class Matrix {
    private final int width;
    private final int height;
    private final NumVector vector;

    public static Matrix create(int height, double... values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("no values");
        } else if (height <= 0) {
            throw new IllegalArgumentException("unexpected height");
        } else {
            int width = values.length / height;
            if (width * height != values.length) {
                throw new IllegalArgumentException("unexpected amount of values");
            } else {
                return new Matrix(width, height, values.clone());
            }
        }
    }

    public static Matrix createIdentity(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("unexpected size");
        } else {
            double[] array = new double[size * size];
            int index = 0;

            for(int i = 0; i < size; index += size + 1) {
                array[index] = 1.0D;
                ++i;
            }

            return new Matrix(size, size, array);
        }
    }

    public static Matrix createColumn(NumVector vector) {
        return new Matrix(1, vector.getSize(), vector);
    }

    public static Matrix createRow(NumVector vector) {
        return new Matrix(vector.getSize(), 1, vector);
    }

    public boolean equals(Object object) {
        if (!(object instanceof Matrix)) {
            return false;
        } else {
            Matrix matrix = (Matrix)object;
            return this.width == matrix.width && this.height == matrix.height && this.vector.equals(matrix.vector);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Matrix ").append(this.height).append("x").append(this.width).append(" { ");
        int i = 0;

        for(int h = 0; h < this.height; ++h) {
            if (h != 0) {
                sb.append(", ");
            }

            sb.append("{");

            for(int w = 0; w < this.width; ++i) {
                if (w != 0) {
                    sb.append(", ");
                }

                sb.append(this.vector.get(i));
                ++w;
            }

            sb.append("}");
        }

        return sb.append(" }").toString();
    }

    public double get(int column, int row) {
        validate(column, this.width);
        validate(row, this.height);
        return this.vector.get(column + row * this.width);
    }

    public int getColumns() {
        return this.width;
    }

    public NumVector getColumn(final int column) {
        validate(column, this.width);
        return new NumVector.Modified(this.vector) {
            @Override
            public double get(int row) {
                Matrix.validate(row, Matrix.this.height);
                return super.get(column + row * Matrix.this.width);
            }
            @Override
            public int getSize() {
                return Matrix.this.height;
            }
        };
    }

    public int getRows() {
        return this.height;
    }

    public NumVector getRow(final int row) {
        validate(row, this.height);
        return new Modified(this.vector) {
            @Override
            public double get(int column) {
                Matrix.validate(column, Matrix.this.width);
                return super.get(column + row * Matrix.this.width);
            }
            @Override
            public int getSize() {
                return Matrix.this.width;
            }
        };
    }

    public Matrix plus(Matrix matrix) {
        if (this.getColumns() != matrix.getColumns()) {
            throw new IllegalArgumentException("columns mismatch");
        } else if (this.getRows() != matrix.getRows()) {
            throw new IllegalArgumentException("rows mismatch");
        } else {
            return new Matrix(this.width, this.height, this.vector.plus(matrix.vector));
        }
    }

    public Matrix minus(Matrix matrix) {
        if (this.getColumns() != matrix.getColumns()) {
            throw new IllegalArgumentException("columns mismatch");
        } else if (this.getRows() != matrix.getRows()) {
            throw new IllegalArgumentException("rows mismatch");
        } else {
            return new Matrix(this.width, this.height, this.vector.minus(matrix.vector));
        }
    }

    public Matrix multiply(double value) {
        return new Matrix(this.width, this.height, this.vector.multiply(value));
    }

    public Matrix multiply(Matrix matrix) {
        if (this.getColumns() != matrix.getRows()) {
            throw new IllegalArgumentException("columns mismatch rows");
        } else {
            int width = matrix.getColumns();
            int height = this.getRows();
            double[] result = new double[width * height];
            int i = 0;

            for(int h = 0; h < height; ++h) {
                NumVector row = this.getRow(h);

                for(int w = 0; w < width; ++i) {
                    result[i] = row.multiply(matrix.getColumn(w));
                    ++w;
                }
            }

            return new Matrix(width, height, result);
        }
    }

    public NumVector multiply(NumVector vector) {
        if (this.getColumns() != vector.getSize()) {
            throw new IllegalArgumentException("columns mismatch length");
        } else {
            double[] result = new double[this.getRows()];

            for(int i = 0; i < result.length; ++i) {
                result[i] = this.getRow(i).multiply(vector);
            }

            return new NumVector(result);
        }
    }

    public double determinant() {
        if (this.width != this.height) {
            throw new IllegalArgumentException("not a square");
        } else if (this.width == 1) {
            return this.vector.get(0);
        } else if (this.width == 2) {
            return this.vector.get(0) * this.vector.get(3) - this.vector.get(1) * this.vector.get(2);
        } else {
            double result = 0.0D;

            for(int i = 0; i < this.width; ++i) {
                double value = this.vector.get(i) * this.exclude(i, 0).determinant();
                result -= isEven(i) ? -value : value;
            }

            return result;
        }
    }

    public Matrix transpose() {
        double[] result = new double[this.vector.getSize()];
        int i = 0;

        for(int w = 0; w < this.width; ++w) {
            for(int h = 0; h < this.height; ++i) {
                result[i] = this.get(w, h);
                ++h;
            }
        }

        return new Matrix(this.height, this.width, result);
    }

    public Matrix inverse() {
        double value = this.determinant();
        if (value == 0.0D) {
            throw new IllegalArgumentException("determinant is 0");
        } else {
            return this.cofactor().transpose().multiply(1.0D / value);
        }
    }

    private Matrix exclude(int column, int row) {
        validate(column, this.width);
        validate(row, this.height);
        int width = this.getColumns() - 1;
        if (width == 0) {
            throw new IllegalArgumentException("cannot exclude last column");
        } else {
            int height = this.getRows() - 1;
            if (height == 0) {
                throw new IllegalArgumentException("cannot exclude last row");
            } else {
                double[] result = new double[width * height];
                int index = 0;
                int i = 0;

                for(int h = 0; h <= height; ++h) {
                    for(int w = 0; w <= width; ++i) {
                        if (w != column && h != row) {
                            result[index++] = this.vector.get(i);
                        }

                        ++w;
                    }
                }

                return new Matrix(width, height, result);
            }
        }
    }

    private Matrix cofactor() {
        if (this.width != this.height) {
            throw new IllegalArgumentException("not a square");
        } else {
            double[] result = new double[this.vector.getSize()];
            int i = 0;

            for(int h = 0; h < this.height; ++h) {
                for(int w = 0; w < this.width; ++i) {
                    double value = this.exclude(w, h).determinant();
                    result[i] = isEven(w) != isEven(h) ? -value : value;
                    ++w;
                }
            }

            return new Matrix(this.width, this.height, result);
        }
    }

    private Matrix(int width, int height, double... values) {
        this(width, height, new NumVector(values));
    }

    private Matrix(int width, int height, NumVector vector) {
        this.width = width;
        this.height = height;
        this.vector = vector;
    }

    private static boolean isEven(int i) {
        return i % 2 == 0;
    }

    private static void validate(int index, int max) {
        if (index < 0 || max <= index) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }
}
