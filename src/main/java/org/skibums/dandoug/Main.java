package org.skibums.dandoug;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.skibums.dandoug.Point.Location;

import com.csvreader.CsvReader;

/**
 * Write an elasticsearch bulk API file to load data from a CSV location
 * file.  
 * See the <a href="http://www.elasticsearch.org/guide/reference/api/bulk/">ElasticSearch Bulk API Guide</a>
 * and the CSV format for
 * <a href="http://www.gps-data-team.com/poi/united_states/restaurants/index.php?poi_data=restaurants&file=Starbucks-US&state=CA-California">data from gps-data-team.com</a>.
 * <p>
 * The resulting file is appropriate to POST to a URL like
 * <pre><code>
 *   http://localhost:9200/<i>index</i>/<i>type</i>/_bulk
 *  </code></pre>
 *  Where <i>index</i> and <i>type</i> are as defined by the application and 
 *  autogeneration of index ids is desired.  
 *  <p>
 *  For example, the file produced, <b>out.json</b>, can be posted to create an index as follows
 *  <pre><code>
 *  curl -s -XPOST elastic.rhcloud.com/points/point/_bulk --data-binary @out.json; echo
 *  </code></pre>
 */
public class Main {
	
	static final String FN = "/Starbucks-US-CA-California.csv";
	static final String INDEX_VERB = "{\"index\" : null }";
	static final String MISSING = null;
	static final String DELIM = ">+";
	
	
	public static void main(String[] args) throws IOException {
		InputStream is = Main.class.getResourceAsStream(FN);
		CsvReader rdr = new CsvReader(is, Charset.defaultCharset());
		
		FileWriter out = new FileWriter("out.json");		
		
		int cnt = 0;
		while (rdr.readRecord()) {
			out.write(INDEX_VERB);
			out.write("\n");
			double lon = Double.parseDouble(rdr.get(0));	
			double lat = Double.parseDouble(rdr.get(1));
			String details = rdr.get(2);
			
			String name, city, street, state, zip, phone;
			StateZipPhone szp;
			// Details can be in nn differt formats
			//   name ';' id
			//   name ';' city ';'  street ';' city ';' state zip '>+' phone ';' id
			//   name ';' street ';' city ';' state zip '>+' phone ';' id
			//   name ';' city ';' state zip '>+' phone ';' id
			//   name ';' street ';' city ';' state zip ';' ';' id
			//   name ';' street ';' city ';' state zip ';' '>+' phone ';' id
			//   name ';' city ';' street ';' city ';' state zip ';' '>+' phone ';' id
			//   basically 2, 4, 6, 7 or 5 ';' delimited fields
			
			String[] parts = details.split(";");
			switch(parts.length) {
			case 2:
				name = parts[0].trim();
				city = MISSING; 
				street = MISSING; 
				state = MISSING; 
				zip = MISSING; 
				phone = MISSING;
				break;
			case 5:
				name = parts[0].trim();
				city = parts[2].trim(); 
				street = parts[1].trim(); 
				szp = parseStateZipPhone(parts[3].trim());
				state = szp.state; 
				zip = szp.zip; 
				phone = szp.phone;
				break;
			case 4:
				name = parts[0].trim();
				city = parts[1].trim();
				// Some times the street address is in the name
				int hyphenIx;
				if ((hyphenIx=name.indexOf('-')) > -1
						&& hyphenIx+1 < name.length()) {
					street = name.substring(hyphenIx+1).trim();
				} else {
					street = MISSING;
				}
				szp = parseStateZipPhone(parts[2].trim());
				state = szp.state; 
				zip = szp.zip; 
				phone = szp.phone;
				break;
			case 6:
				name = parts[0].trim();
				if (parts[4].length()==0) {
					city = parts[2].trim(); 
					street = parts[1].trim(); 
					szp = parseStateZipPhone(parts[3].trim());
				} else {
					if (parts[4].trim().startsWith(DELIM)) {
						city = parts[2].trim(); 
						street = parts[1].trim(); 
						szp = parseStateZipPhone(parts[3].trim());
						szp.phone = parts[4].substring(DELIM.length()).trim(); 
					} else {
						city = parts[3].trim(); 
						street = parts[2].trim(); 
						szp = parseStateZipPhone(parts[4].trim());
					}
				}
				state = szp.state; 
				zip = szp.zip; 
				phone = szp.phone;
				break;
			case 7:
				name = parts[0].trim();
				if (parts[5].length()==0) {
					city = parts[2].trim(); 
					street = parts[1].trim(); 
					szp = parseStateZipPhone(parts[3].trim());
				} else {
					if (parts[5].trim().startsWith(DELIM)) {
						city = parts[3].trim(); 
						street = parts[2].trim(); 
						szp = parseStateZipPhone(parts[4].trim());
						szp.phone = parts[5].substring(DELIM.length()).trim(); 
					} else {
						city = parts[4].trim(); 
						street = parts[2].trim(); 
						szp = parseStateZipPhone(parts[5].trim());
					}
				}
				state = szp.state; 
				zip = szp.zip; 
				phone = szp.phone;
				break;
			default:
				System.err.println("Unable to parse record: "+rdr.getRawRecord());
				continue;
			}
			Point p = new Point(
				PointCategory.STARBUCKS,
				new Location(lat,lon),
				name,
				street,
				city,
				state,
				zip,
				phone);	
			cnt++;	
			out.write(p.toString());
			out.write("\n");

		}
		out.flush();
		out.close();
		System.out.println(String.format("processed %d records",cnt));
	}
	
	private static class StateZipPhone {
		String state = MISSING;
		String zip   = MISSING;
		String phone = MISSING;
	}
	
	// Parsing from 
	//   state zip '>+' phone 
	// State can be more more than one word
	private static StateZipPhone parseStateZipPhone(String str) {
		StateZipPhone result = new StateZipPhone();
		
		// Find the delim
		int delimIx = str.indexOf(DELIM);
		if (delimIx == -1) {
			// no phone number present
			delimIx = str.length();
		}
		String stateZip = str.substring(0,delimIx).trim();
		int zipIx = stateZip.lastIndexOf(' ');
		if (zipIx == -1) {
			System.err.println("Unexpected: "+str);
			return result;
		}
		result.state = Utils.abbreviateState(stateZip.substring(0,zipIx));
		String z = stateZip.substring(zipIx).trim();
		result.zip = (z.length()>5) ? z.substring(0,5) : z;
		if (delimIx != str.length()) {
			result.phone = str.substring(delimIx+DELIM.length()).trim();
		}
		
		return result;
	}


}
