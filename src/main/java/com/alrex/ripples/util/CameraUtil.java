package com.alrex.ripples.util;

import com.alrex.ripples.api.audio.RelativeAngleInFOV;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class CameraUtil {
    public static RelativeAngleInFOV getRelativeAngleOfPointSeenFromCamera(Camera camera, Vec3 point){
        return getRelativeAngleOfPointSeenFromCamera(camera,point,1d);
    }
    public static RelativeAngleInFOV getRelativeAngleOfPointSeenFromCamera(Camera camera, Vec3 point, double radiusHandledAsNearPoint){
        var relativePoint=point.subtract(camera.getPosition());
        var relativePointF=new Vector3f((float) relativePoint.x(), (float) relativePoint.y(), (float) relativePoint.z());
        var relativePointSeenFromCamera=relativePointF.rotate(camera.rotation().conjugate());

        var xyLen=Math.hypot(relativePointSeenFromCamera.x(),relativePointSeenFromCamera.y());
        if (xyLen<radiusHandledAsNearPoint)return RelativeAngleInFOV.EXACT_FRONT;
        var relativePointLen=relativePointSeenFromCamera.length();
        if (relativePointLen<radiusHandledAsNearPoint)return RelativeAngleInFOV.EXACT_FRONT;

        var angleRadian=Math.signum(relativePointSeenFromCamera.x())*Math.acos(relativePointSeenFromCamera.y()/xyLen);
        var distanceRadian=Math.acos(relativePointSeenFromCamera.z()/relativePointLen);

        return new RelativeAngleInFOV((float) angleRadian, (float) distanceRadian);
    }
}
