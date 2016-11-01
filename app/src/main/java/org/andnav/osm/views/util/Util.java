// Created by plusminus on 17:53:07 - 25.09.2008
package org.andnav.osm.views.util;


import com.robert.maps.applib.utils.OSGB36;
import org.andnav.osm.util.BoundingBoxE6;
import org.andnav.osm.views.util.constants.OpenStreetMapViewConstants;

//modify by self 20160916
import com.robert.maps.applib.utils.HK1980GRID;
import com.robert.maps.applib.utils.CentralMapGrid;
import com.robert.maps.applib.utils.TaiwanGrid;
import com.robert.maps.applib.utils.HKMAP2GRID;

/**
 *
 * @author Nicolas Gramlich
 *
 */
public class Util implements OpenStreetMapViewConstants{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static int[] getMapTileFromCoordinates(final int aLat, final int aLon, final int zoom, final int[] reuse, final int aProjection) {
		return getMapTileFromCoordinates(aLat / 1E6, aLon / 1E6, zoom, reuse, aProjection);
	}

	public static int[] getMapTileFromCoordinates(final double aLat, final double aLon, final int zoom, final int[] aUseAsReturnValue, final int aProjection) {
		final int[] out = (aUseAsReturnValue != null) ? aUseAsReturnValue : new int[2];

/*		if (aProjection == 3) {
			final double[] OSRef = OSGB36.LatLon2OSGB(aLat, aLon);
			out[0] = (int) ((1 - OSRef[0] / 1000000)*OpenSpaceUpperBoundArray[zoom - 7]);
			out[1] = (int) ((OSRef[1] / 1000000)*OpenSpaceUpperBoundArray[zoom - 7]);
		} else {
			if (aProjection == 1)
				out[MAPTILE_LATITUDE_INDEX] = (int) Math.floor((1 - Math
						.log(Math.tan(aLat * Math.PI / 180) + 1
								/ Math.cos(aLat * Math.PI / 180))
						/ Math.PI)
						/ 2 * (1 << zoom));
			else {
				final double E2 = (double) aLat * Math.PI / 180;
				final long sradiusa = 6378137;
				final long sradiusb = 6356752;
				final double J2 = (double) Math.sqrt(sradiusa * sradiusa
						- sradiusb * sradiusb)
						/ sradiusa;
				final double M2 = (double) Math.log((1 + Math.sin(E2))
						/ (1 - Math.sin(E2)))
						/ 2
						- J2
						* Math.log((1 + J2 * Math.sin(E2))
								/ (1 - J2 * Math.sin(E2))) / 2;
				final double B2 = (double) (1 << zoom);
				out[MAPTILE_LATITUDE_INDEX] = (int) Math.floor(B2 / 2 - M2 * B2
						/ 2 / Math.PI);
			}

			out[MAPTILE_LONGITUDE_INDEX] = (int) Math.floor((aLon + 180) / 360
					* (1 << zoom));
		}*/

		//modify by self 20161002
		switch (aProjection){
			case 1:
				out[MAPTILE_LATITUDE_INDEX] = (int) Math.floor((1 - Math
						.log(Math.tan(aLat * Math.PI / 180) + 1
								/ Math.cos(aLat * Math.PI / 180))
						/ Math.PI)
						/ 2 * (1 << zoom));
				out[MAPTILE_LONGITUDE_INDEX] = (int) Math.floor((aLon + 180) / 360
						* (1 << zoom));
				break;
			case 2:
				final double E2 = aLat * Math.PI / 180;
				final long sradiusa = 6378137;
				final long sradiusb = 6356752;
				final double J2 = Math.sqrt(sradiusa * sradiusa
						- sradiusb * sradiusb)
						/ sradiusa;
				final double M2 = Math.log((1 + Math.sin(E2))
						/ (1 - Math.sin(E2)))
						/ 2
						- J2
						* Math.log((1 + J2 * Math.sin(E2))
						/ (1 - J2 * Math.sin(E2))) / 2;
				final double B2 = (double) (1 << zoom);
				out[MAPTILE_LATITUDE_INDEX] = (int) Math.floor(B2 / 2 - M2 * B2
						/ 2 / Math.PI);
				out[MAPTILE_LONGITUDE_INDEX] = (int) Math.floor((aLon + 180) / 360
						* (1 << zoom));
				break;
			case 3:
				final double[] OSRef = OSGB36.LatLon2OSGB(aLat, aLon);
				out[0] = (int) ((1 - OSRef[0] / 1000000)*OpenSpaceUpperBoundArray[zoom - 7]);
				out[1] = (int) ((OSRef[1] / 1000000)*OpenSpaceUpperBoundArray[zoom - 7]);
				break;
			case 4:
				final int[] OSRef1 = HK1980GRID.LatLon2GRID(aLat, aLon,zoom);
				out[0] = OSRef1[0];
				out[1] = OSRef1[1];
				break;
			case 5:
				final int[] OSRef2 = CentralMapGrid.LatLon2GRID(aLat, aLon,zoom);
				out[0] = OSRef2[0];
				out[1] = OSRef2[1];
				break;
			case 6:
				final int[] OSRef3 = TaiwanGrid.LatLon2GRID(aLat, aLon,zoom);
				out[0] = OSRef3[0];
				out[1] = OSRef3[1];
				break;
			case 7:
				final int[] OSRef4 = HKMAP2GRID.LatLon2GRID(aLat, aLon,zoom);
				out[0] = OSRef4[0];
				out[1] = OSRef4[1];
				break;
		}
		return out;
	}

	// Conversion of a MapTile to a BoudingBox

	public static BoundingBoxE6 getBoundingBoxFromMapTile(final int[] aMapTile, final int zoom, final int aProjection) {
		final int y = aMapTile[MAPTILE_LATITUDE_INDEX];
		final int x = aMapTile[MAPTILE_LONGITUDE_INDEX];

/*		if(aProjection == 3){
			final double[] LatLon0 = OSGB36.OSGB2LatLon(
					(double)((OpenSpaceUpperBoundArray[zoom - 7] - y - 1) * 1000000
							/ OpenSpaceUpperBoundArray[zoom - 7]), (double)(x * 1000000
							/ OpenSpaceUpperBoundArray[zoom - 7]));
			final double[] LatLon1 = OSGB36.OSGB2LatLon(
					(double)((OpenSpaceUpperBoundArray[zoom - 7] - y - 1 + 1) * 1000000
							/ OpenSpaceUpperBoundArray[zoom - 7]), (double)((x + 1) * 1000000
							/ OpenSpaceUpperBoundArray[zoom - 7]));
			return new BoundingBoxE6(LatLon1[0], LatLon1[1], LatLon0[0], LatLon0[1]);
		} else
			return new BoundingBoxE6(tile2lat(y, zoom, aProjection), tile2lon(x + 1, zoom), tile2lat(y + 1, zoom, aProjection), tile2lon(x, zoom));*/

		//modify by self 20161002
		switch (aProjection){
			case 3:
				final double[] LatLon0 = OSGB36.OSGB2LatLon(
						(double)((OpenSpaceUpperBoundArray[zoom - 7] - y - 1) * 1000000
								/ OpenSpaceUpperBoundArray[zoom - 7]), (double)(x * 1000000
								/ OpenSpaceUpperBoundArray[zoom - 7]));
				final double[] LatLon1 = OSGB36.OSGB2LatLon(
						(double)((OpenSpaceUpperBoundArray[zoom - 7] - y - 1 + 1) * 1000000
								/ OpenSpaceUpperBoundArray[zoom - 7]), (double)((x + 1) * 1000000
								/ OpenSpaceUpperBoundArray[zoom - 7]));
				return new BoundingBoxE6(LatLon1[0], LatLon1[1], LatLon0[0], LatLon0[1]);
			case 4:
				final double[] LatLon2=HK1980GRID.GRID2LatLon(x+1, y, zoom);
				final double[] LatLon3=HK1980GRID.GRID2LatLon(x, y+1, zoom);
				return new BoundingBoxE6(LatLon2[0], LatLon2[1], LatLon3[0], LatLon3[1]);
			case 5:
				final double[] LatLon4=CentralMapGrid.GRID2LatLon(x+1, y, zoom);
				final double[] LatLon5=CentralMapGrid.GRID2LatLon(x, y+1, zoom);
				return new BoundingBoxE6(LatLon4[0], LatLon4[1], LatLon5[0], LatLon5[1]);
			case 6:
				final double[] LatLon6=TaiwanGrid.GRID2LatLon(x+1, y, zoom);
				final double[] LatLon7=TaiwanGrid.GRID2LatLon(x, y+1, zoom);
				return new BoundingBoxE6(LatLon6[0], LatLon6[1], LatLon7[0], LatLon7[1]);
			case 7:
				final double[] LatLon8=HKMAP2GRID.GRID2LatLon(x+1, y, zoom);
				final double[] LatLon9=HKMAP2GRID.GRID2LatLon(x, y+1, zoom);
				return new BoundingBoxE6(LatLon8[0], LatLon8[1], LatLon9[0], LatLon9[1]);

			default:
				return new BoundingBoxE6(tile2lat(y, zoom, aProjection), tile2lon(x + 1, zoom), tile2lat(y + 1, zoom, aProjection), tile2lon(x, zoom));
		}
	}

	private static double tile2lon(int x, int aZoom) {
		return (x / Math.pow(2.0, aZoom) * 360.0) - 180;
	}

	private static double tile2lat(int y, int aZoom, final int aProjection) {

		if (aProjection == 1) {
			final double n = Math.PI - ((2.0 * Math.PI * y) / Math.pow(2.0, aZoom));
			//final double n1 = 180.0 / Math.PI * Math.atan(0.5 * (Math.exp(n) - Math.exp(-n)));
			return 180.0 / Math.PI * Math.atan(0.5 * (Math.exp(n) - Math.exp(-n)));
			
			//return 180 / Math.PI * (2 * Math.atan(Math.exp(a*Math.PI/180)) - Math.PI/2); // http://wiki.openstreetmap.org/wiki/Mercator
		} else {
			final double y1 = 20037508.342789 - 20037508.342789 * 2 / (1 << aZoom) * y;
			
			final double r_major = 6378137.0; //Equatorial Radius, WGS84
			final double r_minor = 6356752.314245179; //defined as constant
			
			double ts = Math.exp ( -y1 / r_major);
	        double phi = Math.PI/2 - 2 * Math.atan(ts);
	        double dphi = 1.0;
	        int i;
	        for (i = 0; Math.abs(dphi) > 0.000000001 && i < 15; i++) {
	                double con = (Math.sqrt(1.0 - (r_minor/r_major * r_minor/r_major))) * Math.sin (phi);
	                dphi = Math.PI/2 - 2 * Math.atan (ts * Math.pow((1.0 - con) / (1.0 + con), (0.5 * (Math.sqrt(1.0 - (r_minor/r_major * r_minor/r_major)))))) - phi;
	                phi += dphi;
	        }
	        return phi / (Math.PI / 180.0);	
			/*
			final double MerkElipsK = 0.0000001;
			final long sradiusa = 6378137;
			final long sradiusb = 6356752;
			final double FExct = (double) Math.sqrt(sradiusa * sradiusa - sradiusb * sradiusb) / sradiusa;
			final int TilesAtZoom = 1 << aZoom;
			double result = (y - TilesAtZoom / 2) / -(TilesAtZoom / (2 * Math.PI));
			result = (2 * Math.atan(Math.exp(result)) - Math.PI / 2) * 180 / Math.PI;
			double Zu = result / (180 / Math.PI);
			double yy = ((y) - TilesAtZoom / 2);

			double Zum1 = Zu;
			Zu = Math.asin(1 - ((1 + Math.sin(Zum1)) * Math.pow(1 - FExct * Math.sin(Zum1), FExct))
					/ (Math.exp((2 * yy) / -(TilesAtZoom / (2 * Math.PI))) * Math.pow(1 + FExct * Math.sin(Zum1), FExct)));
			while (Math.abs(Zum1 - Zu) >= MerkElipsK) {
				Zum1 = Zu;
				Zu = Math.asin(1 - ((1 + Math.sin(Zum1)) * Math.pow(1 - FExct * Math.sin(Zum1), FExct))
						/ (Math.exp((2 * yy) / -(TilesAtZoom / (2 * Math.PI))) * Math.pow(1 + FExct * Math.sin(Zum1), FExct)));
			}

			if (Zu == Float.NaN)
				Zu = 0.0;

			result = Zu * 180 / Math.PI;

			return result;
			*/
		}
	}

	public static int x2lon(int x, int aZoom, final int MAPTILE_SIZEPX) {
		int px = MAPTILE_SIZEPX * (1 << aZoom);
		if (x < 0)
			x = px + x;
		if (x > px)
			x = x - px;
		return (int) (1E6 * (((double)x / px * 360.0) - 180));
	}

	public static double y2lat(int y, int aZoom, final int MAPTILE_SIZEPX) {
//		final int aProjection = 1;

//		if (aProjection == 1) {
			final double n = Math.PI
					- ((2.0 * Math.PI * y) / MAPTILE_SIZEPX * Math.pow(2.0, aZoom));
			return 180.0 / Math.PI
					* Math.atan(0.5 * (Math.exp(n) - Math.exp(-n)));
//		} else {
//			final double MerkElipsK = 0.0000001;
//			final long sradiusa = 6378137;
//			final long sradiusb = 6356752;
//			final double FExct = (double) Math.sqrt(sradiusa * sradiusa
//					- sradiusb * sradiusb)
//					/ sradiusa;
//			final int TilesAtZoom = 1 << aZoom;
//			double result = (y - TilesAtZoom / 2)
//					/ -(TilesAtZoom / (2 * Math.PI));
//			result = (2 * Math.atan(Math.exp(result)) - Math.PI / 2) * 180
//					/ Math.PI;
//			double Zu = result / (180 / Math.PI);
//			double yy = ((y) - TilesAtZoom / 2);
//
//			double Zum1 = Zu;
//			Zu = Math
//					.asin(1
//							- ((1 + Math.sin(Zum1)) * Math.pow(1 - FExct
//									* Math.sin(Zum1), FExct))
//							/ (Math.exp((2 * yy)
//									/ -(TilesAtZoom / (2 * Math.PI))) * Math
//									.pow(1 + FExct * Math.sin(Zum1), FExct)));
//			while (Math.abs(Zum1 - Zu) >= MerkElipsK) {
//				Zum1 = Zu;
//				Zu = Math
//						.asin(1
//								- ((1 + Math.sin(Zum1)) * Math.pow(1 - FExct
//										* Math.sin(Zum1), FExct))
//								/ (Math.exp((2 * yy)
//										/ -(TilesAtZoom / (2 * Math.PI))) * Math
//										.pow(1 + FExct * Math.sin(Zum1), FExct)));
//			}
//
//			result = Zu * 180 / Math.PI;
//
//			return result;
//		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
