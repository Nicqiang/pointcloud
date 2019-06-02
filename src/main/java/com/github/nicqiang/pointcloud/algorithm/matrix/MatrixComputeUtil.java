package com.github.nicqiang.pointcloud.algorithm.matrix;

import lombok.extern.slf4j.Slf4j;
import org.ejml.data.Complex_F64;
import org.ejml.simple.SimpleEVD;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-02
 */
@Slf4j
public class MatrixComputeUtil {

    public static List<Double> eigen(float[] point, float[][] points){
        int k = points.length;
        float pavg[] = getPAvg(points);

        SimpleMatrix pv = new SimpleMatrix(pavg.length, 1, true, pavg);
        SimpleMatrix oneMatrix = getOneMatrix(1, k);

        SimpleMatrix P = new SimpleMatrix(points).transpose();

        SimpleMatrix pp = P.minus(pv.mult(oneMatrix));

        SimpleMatrix C = pp.mult(pp.transpose());

        C = C.divide(k);

        SimpleEVD<SimpleMatrix> eig = C.eig();

        List<Double> eigf = new ArrayList<>();
        for (Complex_F64 eigenvalue : eig.getEigenvalues()) {
            eigf.add(eigenvalue.real);
        }
        eigf.sort(Comparator.naturalOrder());

        return eigf;

    }

    /**
     * row * colum 的 one matrix
     * @param row
     * @param colum
     * @return
     */
    private static SimpleMatrix getOneMatrix(int row, int colum) {
        float[][] values = new float[row][colum];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < colum; j++) {
                values[i][j] = 1;
            }
        }
        return new SimpleMatrix(values);
    }

    private static float[] getPAvg(float[][] points){
        int k = points.length;
        float pavg[] = {0f, 0f, 0f};
        for (float[] np : points) {
            pavg[0] += np[0];
            pavg[1] += np[1];
            pavg[2] += np[2];
        }
        for (int i = 0; i < pavg.length; i++) {
            pavg[i] /= k;
        }
        return pavg;
    }

}
