package com.fresco;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Interface for implementing a controller that works with {@link ZoomableDraweeView}
 * to control the zoom.
 */
public interface ZoomableController {

	/**
	 * Listener interface.
	 */
	public interface Listener {

		/**
		 * Notifies the view that the transform changed.
		 *
		 * @param transform the new matrix
		 */
		void onTransformChanged(Matrix transform);
	}

	/**
	 * Enables the controller. The controller is enabled when the image has been loaded.
	 *
	 * @param enabled whether to enable the controller
	 */
	void setEnabled(boolean enabled);

	/**
	 * Gets whether the controller is enabled. This should return the last value passed to
	 * {@link #setEnabled}.
	 *
	 * @return whether the controller is enabled.
	 */
	boolean isEnabled();

	/**
	 * Sets the listener for the controller to call back when the matrix changes.
	 *
	 * @param listener the listener
	 */
	void setListener(Listener listener);

	/**
	 * Gets the current scale factor. A convenience method for calculating the scale from the
	 * transform.
	 *
	 * @return the current scale factor
	 */
	float getScaleFactor();

	/**
	 * Gets the current transform.
	 *
	 * @return the transform
	 */
	Matrix getTransform();

	/**
	 * Sets the bounds of the image post transform prior to application of the zoomable
	 * transformation.
	 *
	 * @param imageBounds the bounds of the image
	 */
	void setImageBounds(RectF imageBounds);

	/**
	 * Sets the bounds of the view.
	 *
	 * @param viewBounds the bounds of the view
	 */
	void setViewBounds(RectF viewBounds);

	/**
	 * Allows the controller to handle a touch event.
	 *
	 * @param event the touch event
	 * @return whether the controller handled the event
	 */
	boolean onTouchEvent(MotionEvent event);
}