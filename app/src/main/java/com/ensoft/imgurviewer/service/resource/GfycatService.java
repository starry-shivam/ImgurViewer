package com.ensoft.imgurviewer.service.resource;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ensoft.imgurviewer.model.GfycatVideo;
import com.ensoft.imgurviewer.service.listener.PathResolverListener;
import com.ensoft.imgurviewer.service.network.RequestQueueService;
import com.google.gson.Gson;

import org.json.JSONObject;

public class GfycatService extends ImageServiceSolver
{
	public static final String TAG = GyazoService.class.getCanonicalName();
	public static final String GFYCAT_DOMAIN = "gfycat.com";
	public static final String GFYCAT_INFO_URL = "https://gfycat.com/cajax/get/";

	protected String getResourceName( Uri uri )
	{
		return uri.getPath().replace( "/", "" );
	}

	protected String getResourcePath( Uri uri )
	{
		return GFYCAT_INFO_URL + getResourceName( uri );
	}

	@Override
	public void getPath( Uri uri, final PathResolverListener pathResolverListener )
	{
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( getResourcePath( uri ), null, new Response.Listener<JSONObject>()
		{
			@Override
			public void onResponse(JSONObject response)
			{
				try
				{
					Log.v( TAG, response.toString() );

					JSONObject item = response.getJSONObject( "gfyItem" );

					GfycatVideo video = new Gson().fromJson( item.toString(), GfycatVideo.class );

					pathResolverListener.onPathResolved( video.getUri(), null );
				}
				catch ( Exception e )
				{
					Log.v( TAG, e.getMessage() );

					pathResolverListener.onPathError( e.toString() );;
				}
			}
		}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Log.v( TAG, error.toString() );

				pathResolverListener.onPathError( error.toString() );
			}
		});

		RequestQueueService.getInstance().addToRequestQueue( jsonObjectRequest );
	}

	@Override
	public boolean isServicePath( Uri uri )
	{
		return uri.toString().contains( GFYCAT_DOMAIN );
	}
}
