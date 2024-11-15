/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.engineer.panorama.location;

import android.location.Location;
import android.os.Bundle;

public interface BDLocationListener {
    void onLocationChanged(Location location);
    void onStatusChanged(String provider, int status, Bundle extras);
    void onProviderEnabled(String provider);
    void onProviderDisabled(String provider);
}
