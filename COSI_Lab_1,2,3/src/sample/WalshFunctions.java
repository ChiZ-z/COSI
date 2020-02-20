package sample;

import java.util.ArrayList;

public class WalshFunctions
{
    private int base = 2;

    private int N = 8;
    private int rademacher_N = (int)(Math.log((double)N) / Math.log(base));

    private int[][] hadamard_matrix = new int[N][N];
    private int[][] rademacher_matrix = new int[rademacher_N][N];
    private int[][] walsh_matrix_by_rademacher = new int[N][N];

    private int[][] rademacher_powers = new int[N][rademacher_N];


    public WalshFunctions()
    {
        FormHadamardMatrix();
        FormRademacherFunctions();
        FormWalshFunctionByRademacher();
    }

    public int[][] GetHadamardMarix()
    {
        return this.hadamard_matrix;
    }

    public int[][] GetWalshFunctionsByRademacher()
    {
        return this.walsh_matrix_by_rademacher;
    }

    private void FormHadamardMatrix()
    {
        this.hadamard_matrix[0][0] = 1;

        for(int k = 1; k < N; k += k)
        {
            for(int i = 0; i < k; i++)
            {
                for(int j = 0; j < k; j++)
                {
                    this.hadamard_matrix[i + k][j] = this.hadamard_matrix[i][j];
                    this.hadamard_matrix[i][j + k] = this.hadamard_matrix[i][j];
                    this.hadamard_matrix[i + k][j + k] = (-1) * this.hadamard_matrix[i][j];
                }
            }
        }
    }

    private void FormRademacherFunctions()
    {
        for(int i = 0; i < rademacher_N; i++)
        {
            int k = N / (int)Math.pow(base, i + 1);
            int counter = 1;
            int factor = 1;
            for(int j = 0; j < N; j++, counter++)
            {
                this.rademacher_matrix[i][j] = factor;
                if(counter == k)
                {
                    counter = 0;
                    factor *= -1;
                }
            }
        }
    }

    private void FormRademacherPowers()
    {
        for(int n = 0; n < N; n++)
        {
            int[] n_bitmask = ConvertToBitmask(n);

            for(int k = 1; k <= rademacher_N; k++)
            {
                this.rademacher_powers[n][k - 1] = Xor(n_bitmask[rademacher_N - k + 1], n_bitmask[rademacher_N - k], 0);
            }
        }

    }

    private void FormWalshFunctionByRademacher()
    {
        FormRademacherPowers();

        for(int n = 0; n < N ; n++)
        {
            ArrayList<Integer> power_index = new ArrayList<>();

            for(int j = 0; j < rademacher_N; j++)
            {
                if(rademacher_powers[n][j] > 0)
                {
                    power_index.add(j);
                }
            }

            if(power_index.isEmpty())
            {
                this.walsh_matrix_by_rademacher[n] = GetWalshFunctionZero();
            }
            else if(power_index.size() == 1)
            {
                this.walsh_matrix_by_rademacher[n] = this.rademacher_matrix[power_index.get(0)];
            }
            else
            {
                this.walsh_matrix_by_rademacher[n] = MultiplyRademacherFunctions(power_index);
            }
        }
    }

    private int[] ConvertToBitmask(int value)
    {
        int[] binary_value = new int[rademacher_N + 1];
        int i;

        for(i = 0; value >= base; i++)
        {
            binary_value[i] = value % base;
            value /= base;
        }
        binary_value[i] = value;
        int[] result = new int[rademacher_N + 1];

        for(int j = rademacher_N + 1; j > 0 ; j--)
        {
            result[rademacher_N + 1 - j] = binary_value[j - 1];
        }

        return result;
    }

    private int Xor(int a, int b, int mode)
    {
        if(a == b)
        {
            if(mode == 0)
            {
                return 0;
            }
            else
            {
                return 1;
            }

        }
        else
        {
            if(mode == 0)
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
    }

    private int[] GetWalshFunctionZero()
    {
        int[] result = new int[N];

        for(int j = 0; j < N; j++)
        {
            result[j] = 1;
        }

        return result;
    }

    private int[] MultiplyRademacherFunctions(ArrayList<Integer> indexes)
    {
        int[] result = this.rademacher_matrix[indexes.get(0)].clone();

        for(int i = 1; i < indexes.size(); i++)
        {
            for(int j = 0; j < N; j++)
            {
                result[j] = Xor(result[j], this.rademacher_matrix[indexes.get(i)][j], 1);
            }
        }

        return result;
    }
}
