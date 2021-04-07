package fr.cpe.gr6.audioplayer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FilesFromWebserviceTask extends AsyncTask<Object, Integer, ArrayList<AudioFile>> {

    @Override
    protected ArrayList<AudioFile> doInBackground(Object... param) {
        String urlString= (String) param[0];
        Context context= (Context) param[1];
        String type= (String) param[2];

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line, response = "";
                while ((line = in.readLine()) != null) {
                    response += line;
                }
                ArrayList<AudioFile> final_list_audio = null;
                switch(type) {
                    case "artist":
                        final_list_audio =parseArtistJson(response);
                        MainActivity_lastfm_artist.fakeList=final_list_audio;
                        break;
                    case "album":
                        final_list_audio =parseAlbumJson(response);
                        MainActivity_lastfm_album.fakeList=final_list_audio;
                        break;
                    case "genre":
                        final_list_audio =parseTagJson(response, context);
                        MainActivity_lastfm.fakeList=final_list_audio;
                        break;
                }

                System.out.println("__________result="+ final_list_audio);
                in.close();
                Log.d("TAG", "doInBackground: Fin de la récupération du flux");

                return final_list_audio;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


    protected void onPostExecute(ArrayList<AudioFile> result) {
        super.onPostExecute(result);
    }

    protected ArrayList<AudioFile> parseTagJson(String jsonString, Context context) throws JSONException {
        System.out.println("_______________________in parseTagJson");

        //BASE DE DONNEES=========================================================
        //myDbAdapter helper=new myDbAdapter(context);
        //helper.creaionBd();
        //long id = helper.insertData("tag","rock","https://www.last.fm/tag/rock");
        //=========================================================================

        ArrayList<AudioFile> list_son=new ArrayList<AudioFile>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject tags = jsonObject.getJSONObject("tags");
        JSONArray alltag = tags.getJSONArray("tag");
        //String StringInsertInDb="INSERT INTO 'tag' ('tagname', 'tagurl') VALUES ";
        for (int i = 0; i < alltag.length(); i++) {
            JSONObject tag = alltag.getJSONObject(i);
            String tagName = tag.getString("name");
            String tagUrl = tag.getString("url");
            AudioFile a=new AudioFile(tagName);
            list_son.add(a);

            //BASE DE DONNEES=========================================================
            /* if(i!=0){
                StringInsertInDb +=",";
            }
             StringInsertInDb += "('"+tagName+"', '"+tagUrl+"')";*/
            //=========================================================================
        }
        //StringInsertInDb += ";";
        return list_son;
    }

    protected ArrayList<AudioFile> parseTagTopTrackJson(String jsonString) throws JSONException {
        ArrayList<AudioFile> list=new ArrayList<AudioFile>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject tracks = jsonObject.getJSONObject("tracks");
        JSONArray alltrack = tracks.getJSONArray("track");
        for (int i = 0; i < alltrack.length(); i++) {
            JSONObject track = alltrack.getJSONObject(i);
            String trackName = track.getString("name");
            String trackUrl = track.getString("url");
            int trackduration = track.getInt("duration");
            JSONObject artistobj = track.getJSONObject("artist");
            String artistName = track.getString("name");
            AudioFile a=new AudioFile(trackName,"String filePath", artistName, "String album", "String genre", 2020, trackduration);
            list.add(a);
        }
        return list;
    }

    protected ArrayList<AudioFile> parseArtistJson(String jsonString) throws JSONException {
        ArrayList<AudioFile> list=new ArrayList<AudioFile>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject tracks = jsonObject.getJSONObject("artists");
        JSONArray alltrack = tracks.getJSONArray("artist");
        for (int i = 0; i < alltrack.length(); i++) {
            JSONObject track = alltrack.getJSONObject(i);
            String Name = track.getString("name");
            String url = track.getString("url");
            AudioFile a=new AudioFile(Name,url, Name, "", "", 2020, 0);
            list.add(a);
        }
        return list;
    }

    protected ArrayList<AudioFile> parseAlbumJson(String jsonString) throws JSONException {
        ArrayList<AudioFile> list=new ArrayList<AudioFile>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject tracks = jsonObject.getJSONObject("topalbums");
        JSONArray alltrack = tracks.getJSONArray("album");
        for (int i = 0; i < alltrack.length(); i++) {
            JSONObject track = alltrack.getJSONObject(i);
            String Name = track.getString("name");
            String url = track.getString("url");
            int nbtrack = track.getInt("playcount");
            AudioFile a=new AudioFile(Name,url, Name, Name, "String genre", 2020, nbtrack);
            list.add(a);
        }
        return list;
    }

}
