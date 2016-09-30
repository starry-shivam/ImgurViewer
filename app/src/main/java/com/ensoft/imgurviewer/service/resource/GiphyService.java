package com.ensoft.imgurviewer.service.resource;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ensoft.imgurviewer.App;
import com.ensoft.imgurviewer.model.GiphyResource;
import com.ensoft.imgurviewer.service.listener.PathResolverListener;
import com.ensoft.restafari.network.service.RequestService;
import com.google.gson.Gson;
import com.imgurviewer.R;

import org.json.JSONObject;

public class GiphyService extends ImageServiceSolver
{
	public static final String TAG = GiphyService.class.getCanonicalName();
	public static final String GIPHY_DOMAIN = "giphy.com";
	public static final String GIPHY_API_URL = "https://api.giphy.com/v1/gifs/%s?api_key=%s";

	protected String getId( Uri uri )
	{
		String url = uri.toString();
		String[] split = url.split( "-" );

		if ( split.length > 0 )
		{
			return split[ split.length - 1 ];
		}

		return null;
	}

	@Override
	public void getPath( Uri uri, final PathResolverListener pathResolverListener )
	{
		try
		{
			String id = getId( uri );

			if ( null != id )
			{
				String apiUrl = String.format( GIPHY_API_URL, id, App.getInstance().getString( R.string.giphy_api_key ) );

				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( apiUrl, null, new Response.Listener<JSONObject>()
				{
					@Override
					public void onResponse( JSONObject response )
					{
						try
						{
							Log.v( TAG, response.toString() );

							GiphyResource giphyResource = new Gson().fromJson( response.toString(), GiphyResource.class );

							if ( 200 == giphyResource.getStatus() )
							{
								pathResolverListener.onPathResolved( giphyResource.getData().getUri(), null );
							}
							else
							{
								pathResolverListener.onPathError( App.getInstance().getString( R.string.unknown_error ) );
							}
						}
						catch ( Exception e )
						{
							Log.v( TAG, e.getMessage() );

							pathResolverListener.onPathError( e.toString() );
						}
					}
				}, new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse( VolleyError error )
					{
						Log.v( TAG, error.toString() );

						pathResolverListener.onPathError( error.toString() );
					}
				} );

				RequestService.getInstance().addToRequestQueue( jsonObjectRequest );
			}
		}
		catch ( Exception e )
		{
			Log.v( TAG, e.getMessage() );

			pathResolverListener.onPathError( e.toString() );
		}
	}

	@Override
	public boolean isServicePath( Uri uri )
	{
		String uriStr = uri.toString();

		return (	uriStr.startsWith( "https://" + GIPHY_DOMAIN ) ||
			uriStr.startsWith( "http://" + GIPHY_DOMAIN ) ||
			uriStr.startsWith( "https://www." + GIPHY_DOMAIN ) ||
			uriStr.startsWith( "http://www." + GIPHY_DOMAIN ));
	}

	@Override
	public boolean isGallery( Uri uri )
	{
		return false;
	}
}