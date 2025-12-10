package de.uni.ppr.loader;

import de.uni.ppr.model.Abgeordnete;
import org.json.JSONArray;
import org.json.JSONObject;
import org.texttechnologylab.utilities.FileUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AbgeordneteLoader {

    public static List<Abgeordnete> loadFromJson(String filePath) throws Exception {

        String content = FileUtilities.readContent(new File(filePath));


        JSONArray jsonArray = new JSONArray(content);
        List<Abgeordnete> abgeordneteList = new ArrayList<>();


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            Abgeordnete a = new Abgeordnete();
            a.setID(obj.optString("ID"));
            a.setName(obj.optString("name"));
            a.setVorname(obj.optString("vorname"));
            a.setBeruf(obj.optString("beruf"));
            a.setWp(obj.optString("WP"));


            JSONArray membershipArray = obj.optJSONArray("membership");
            if (membershipArray != null) {
                List<String> memberships = new ArrayList<>();
                for (int j = 0; j < membershipArray.length(); j++) {
                    memberships.add(membershipArray.getString(j));
                }
                a.setMembership(memberships);
            }

            abgeordneteList.add(a);
        }

        return abgeordneteList;
    }






}
