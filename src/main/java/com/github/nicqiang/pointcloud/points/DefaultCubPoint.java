package com.github.nicqiang.pointcloud.points;

import com.github.nicqiang.pointcloud.domain.PointCloud;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-05-31
 */
public class DefaultCubPoint {
    private static final Random random = new Random(System.currentTimeMillis());

    public static PointCloud getPointCloud(int num){
        if(num == 0){
            num = 10000;
        }
        PointCloud pointCloud = new PointCloud();
        //生成10万个点1
        //Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        //-5 ~ 5之间，面上的点
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        /*for (int i = 0; i < 100; i++) {
            double x = Double.parseDouble(decimalFormat.format(random.nextDouble()*10-5));
            double y = Double.parseDouble(decimalFormat.format(random.nextDouble()*10-5));
            double z = Double.parseDouble(decimalFormat.format(random.nextDouble()*10-5));
            sb.append(x);
            sb.append(",");
            sb.append(y);
            sb.append(",");
            sb.append(z);
            sb.append(";");
        }*/
        for (int i = 0; i < num; i++) {
            int flag = random.nextInt(4) % 3;
            sb.append(flag == 0 ?  getPintCord(5) : Double.parseDouble(decimalFormat.format(random.nextDouble()*10-5)));
            sb.append(",");
            sb.append(flag == 1 ?  getPintCord(5) : Double.parseDouble(decimalFormat.format(random.nextDouble()*10-5)));
            sb.append(",");
            sb.append(flag == 2 ?  getPintCord(5) : Double.parseDouble(decimalFormat.format(random.nextDouble()*10-5)));
            sb.append(";");

        }
        sb.deleteCharAt(sb.length()-1);
        pointCloud.setPoints(sb.toString());
        return pointCloud;
    }

    private static double getPintCord(double i) {
        return random.nextBoolean() ? i : -i;
    }

    public static PointCloud getNosityPoint(int num){
        if(num == 0){
            num = 10000;
        }
        PointCloud pointCloud = new PointCloud();
        //生成10万个点1
        //Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        //-5 ~ 5之间，面上的点
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        for (int i = 0; i < num; i++) {
            int flag = random.nextInt(4) % 3;
            sb.append(flag == 0 ?  getPintCordWithNosiy(5) : Double.parseDouble(decimalFormat.format(random.nextDouble()*10-5)));
            sb.append(",");
            sb.append(flag == 1 ?  getPintCordWithNosiy(5) : Double.parseDouble(decimalFormat.format(random.nextDouble()*10-5)));
            sb.append(",");
            sb.append(flag == 2 ?  getPintCordWithNosiy(5) : Double.parseDouble(decimalFormat.format(random.nextDouble()*10-5)));
            sb.append(";");

        }
        sb.deleteCharAt(sb.length()-1);
        pointCloud.setPoints(sb.toString());
        return pointCloud;
    }

    /**
     * 有噪声的点云
     * @param num
     * @return
     */
    public static PointCloud getPointCloudWithNosiy(int num) {
        PointCloud pointCloud = getPointCloud(num);
        PointCloud pointCloudWithNosiy = getNosityPoint(200);
        pointCloud.setPoints(pointCloud.getPoints()+";"+pointCloudWithNosiy.getPoints());
        return pointCloud;

    }
    private static double getPintCordWithNosiy(double i) {
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        double abs = Math.abs(i);
        double inc = Double.parseDouble(decimalFormat.format(Math.abs(random.nextDouble() * 1)));
        double number = abs + inc;
        return random.nextBoolean() ? number : -number;
    }

}
