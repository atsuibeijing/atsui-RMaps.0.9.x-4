//modify by self 20161001
//add HKMAP2GRID class
package com.robert.maps.applib.utils;

import com.robert.maps.applib.utils.Transform;

public class HKMAP2GRID {
    public static int relativePositionInCenterMapTile[]={0,0} ;
    final static int IMAGE_WIDTH =256;
    final static int IMAGE_HEIGHT= 256;
    final static double origin[]={-4786700.0,8353100.0};
    public final static double resolution[] = {42.33341800016934, 21.16670900008467, 10.583354500042335, 5.291677250021167, 2.6458386250105836, 1.3229193125052918, 0.6614596562526459, 0.33072982812632296, 0.16536491406316148};

/*    level          resolution  scale
    -----          ----------  -----
            0   42.33341800016934 160000
            1   21.16670900008467  80000
            2  10.583354500042335  40000
            3   5.291677250021167  20000
            4  2.6458386250105836  10000
            5  1.3229193125052918   5000
            6  0.6614596562526459   2500
            7 0.33072982812632296   1250
            8 0.16536491406316148    625    */

    public static int[] LatLon2GRID(final double Latitude, final double Longitude, final int zoom) {
        Transform transform = new Transform();
        GridData griddata1 = transform.GEOHK(2D, Latitude, Longitude);

        double[] a=new double[2];
        a[0]=griddata1.getE();  //X
        a[1]=griddata1.getN();  //Y

        int x = (int) Math.floor((a[0] - origin[0]) / (IMAGE_WIDTH  * resolution[zoom-1]));
        int y = (int) Math.floor((origin[1] - a[1]) / (IMAGE_HEIGHT * resolution[zoom-1]));

        relativePositionInCenterMapTile[1] = (int) Math.floor(Math.abs((a[0] - (origin[0] + x * (IMAGE_WIDTH * resolution[zoom-1]))) / resolution[zoom-1]));
        relativePositionInCenterMapTile[0] = (int) Math.floor(Math.abs((a[1] - (origin[1] - y * (IMAGE_HEIGHT * resolution[zoom-1]))) / resolution[zoom-1]));
/*
        x1 = Math.floor((a.x - origin.x) / (d.width * b.resolution));
        y1 = Math.floor((origin.y - a.y) / (d.height * b.resolution));

        x2 = Math.floor(Math.abs[(a.x - (origin.x + x1 * (d.width * b.resolution))) / b.resolution]) + g.x;
        y2 = Math.floor(Math.abs[(a.y - (origin.y - y1 * (d.height * b.resolution))) / b.resolution]) + g.y;
*/

//        if (relativePositionInCenterMapTile[1]==0) x--;
//        if (relativePositionInCenterMapTile[0]==0) y--;

        int[] ret = new int[2];
        ret[1]=x;
        ret[0]=y;

        return ret;
    }

    public static double[] GRID2LatLon(final int xGRID, final int yGRID, final int zoom){
        double x=xGRID*IMAGE_WIDTH;
        double y=yGRID*IMAGE_HEIGHT;

        double[] l=new double[2];
        l[0]=x*resolution[zoom-1]+origin[0];
        l[1]=origin[1]-y*resolution[zoom-1];

        Transform transform = new Transform();
        GeoData geodata1 = transform.HKGEO(2D, l[1], l[0]);

        double[] ret = new double[2];
        ret[0] = geodata1.getPhi();
        ret[1] = geodata1.getFlam();
        return ret;
    }

}
 