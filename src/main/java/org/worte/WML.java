package main.java.org.worte;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WML {
	
	private JSONParser JSON_PARSER     = new JSONParser();
	private String     MANIFEST_URL    = "https://launchermeta.mojang.com/mc/game/version_manifest_v2.json";
	private String     ROOT_DIRECTORY  = "C:\\Users\\worte\\Desktop\\WML";
	
	public WML(String VersionID) throws Exception {
		
		// -- ManifestFetching + VersionsFolder --
		String VersionsRootDirectory = ROOT_DIRECTORY + "/versions/";
		
		URL               ManifestURL               = new URL(MANIFEST_URL);
		URLConnection     ManifestURLConnection     = ManifestURL.openConnection();	
		InputStream       ManifestInputStream       = ManifestURLConnection.getInputStream();
		
		File ManifestFile = new File(VersionsRootDirectory + "/version_manifest_v2.json");
		File ManifestDirectory = new File(VersionsRootDirectory);
		
		if (!ManifestFile.isFile()) {
			
			if (!ManifestDirectory.isDirectory()) {
				
				ManifestDirectory.mkdirs();
			}
			
			FileOutputStream  ManifestFileOutputStream  = new FileOutputStream(ManifestFile);
            		Integer           ManifestBytesRead         = -1;
            		byte[]            ManifestBuffer            = new byte[4096];
            
            while ((ManifestBytesRead = ManifestInputStream.read(ManifestBuffer)) != -1) {
            	
            	ManifestFileOutputStream.write(ManifestBuffer, 0, ManifestBytesRead);
            	
            }
			
            ManifestFileOutputStream.close();
		}
		
		InputStreamReader ManifestInputStreamReader = new InputStreamReader(ManifestInputStream);
		BufferedReader    ManifestBufferedReader    = new BufferedReader(ManifestInputStreamReader);
		String            ManifestString            = ManifestBufferedReader.readLine();
		// -- ManifestFetching + VersionsFolder --
		
		
		// -- ManifestParsing --
		JSONObject ManifestJSONObject =  (JSONObject) JSON_PARSER.parse(ManifestString);
		// -- ManifestParsing --
		
		// -- Metadata --
		JSONArray ManifestVersions = (JSONArray) ManifestJSONObject.get("versions");
		
		for (Object ManifestVersion : ManifestVersions) {
			
			JSONObject ManifestVersionJSONObject = (JSONObject) ManifestVersion;
			String     ManifestVersionID         = (String)     ManifestVersionJSONObject.get("id");
			
			if (ManifestVersionID.equals(VersionID)) {
				
				// -- MetadataFetching + VersionsFolder --
				String            MetadataURLString         = (String) ManifestVersionJSONObject.get("url");
				URL               MetadataURL               = new URL(MetadataURLString);
				URLConnection     MetadataURLConnection     = MetadataURL.openConnection();
				InputStream       MetadataInputStream       = MetadataURLConnection.getInputStream();
				
				String VersionsFileDirectory = VersionsRootDirectory + "/" + ManifestVersionID;
				File   MetadataFile          = new File(VersionsFileDirectory + "/" + ManifestVersionID + ".json");
				File   MetadataDirectory     = new File(VersionsFileDirectory);
				
				if (!MetadataFile.isFile()) {
					
					if (!MetadataDirectory.isDirectory()) {
						
						MetadataDirectory.mkdirs();
						
					}
					
					FileOutputStream  MetadataFileOutputStream  = new FileOutputStream(MetadataFile);
					    Integer           MetadataBytesRead         = -1;
					    byte[]            MetadataBuffer            = new byte[4096];
		            
					    while ((MetadataBytesRead = MetadataInputStream.read(MetadataBuffer)) != -1) {

						MetadataFileOutputStream.write(MetadataBuffer, 0, MetadataBytesRead);

					    }

					    MetadataFileOutputStream.close();
				}
				
				InputStreamReader MetadataInputStreamReader = new InputStreamReader(MetadataInputStream);
				BufferedReader    MetadataBufferedReader    = new BufferedReader(MetadataInputStreamReader);
				String            MetadataString            = MetadataBufferedReader.readLine();
				// -- MetadataFetching + VersionsFolder --
				
				// -- MetadataParsing --
				JSONObject MetadataJSONObject = (JSONObject) JSON_PARSER.parse(MetadataString);
				// -- MetadataParsing --
				
				// -- AssetsFolder --
				String AssetsRootDirectory = ROOT_DIRECTORY + "/assets/";
				
				// -- AssetsFolderIndexes --
				String IndexesRootDirectory = AssetsRootDirectory + "/indexes/";
				
				JSONObject MetadataAssetIndexJSONObject = (JSONObject) MetadataJSONObject.get("assetIndex");	
				
				String        AssetIndexURLString     = (String) MetadataAssetIndexJSONObject.get("url");
				URL           AssetIndexURL           = new URL(AssetIndexURLString);
				URLConnection AssetIndexURLConnection = AssetIndexURL.openConnection();
				InputStream   AssetIndexInputStream   = AssetIndexURLConnection.getInputStream();
				
				File   AssetIndexFile          = new File(IndexesRootDirectory + MetadataAssetIndexJSONObject.get("id") + ".json");
				File   AssetIndexDirectory     = new File(IndexesRootDirectory);
				
				if (!AssetIndexFile.isFile()) {
					
					if (!AssetIndexDirectory.isDirectory()) {
						
						AssetIndexDirectory.mkdirs();
						
					}
					
					FileOutputStream  AssetIndexFileOutputStream  = new FileOutputStream(AssetIndexFile);
	                		Integer           AssetIndexBytesRead         = -1;
	                		byte[]            AssetIndexBuffer            = new byte[4096];
	                
					while ((AssetIndexBytesRead = AssetIndexInputStream.read(AssetIndexBuffer)) != -1) {

						AssetIndexFileOutputStream.write(AssetIndexBuffer, 0, AssetIndexBytesRead);

					}
					
					AssetIndexFileOutputStream.close();
				}
				
				
				// -- AssetsFolderIndexes --
				
				// -- AssetsFolderObjects --
				String ObjectsRootDirectory = AssetsRootDirectory + "/objects/"; 
				
				InputStreamReader AssetIndexInputStreamReader = new InputStreamReader(AssetIndexInputStream);
				BufferedReader    AssetIndexBufferedReader    = new BufferedReader(AssetIndexInputStreamReader);
				String            AssetIndexString            = AssetIndexBufferedReader.readLine();
				
				JSONObject AssetIndexJSONObject  = (JSONObject) JSON_PARSER.parse(AssetIndexString);
				JSONObject  ObjectsJSONObject     = (JSONObject) AssetIndexJSONObject.get("objects");    

				for (Object Object : ObjectsJSONObject.values()) {

					JSONObject ObjectJSONObject = (JSONObject) Object;
					
					String ObjectHash = (String) ObjectJSONObject.get("hash");
					String ObjectPath = ObjectHash.substring(0, 2) + "/" + ObjectHash;
					
					String ObjectFileDirectory = ObjectsRootDirectory + ObjectPath;
					File ObjectFile       = new File(ObjectFileDirectory);
					File ObjectDirectory  = new File(ObjectsRootDirectory + ObjectHash.substring(0, 2));
					
					
					if (!ObjectFile.isFile()) {
						
						if (!ObjectDirectory.isDirectory()) {
							
							ObjectDirectory.mkdirs();
							
						}
						
						String            ObjectURLString         = "http://resources.download.minecraft.net/" + ObjectPath;
						URL               ObjectURL               = new URL(ObjectURLString);
						URLConnection     ObjectURLConnection     = ObjectURL.openConnection();
						InputStream       ObjectInputStream       = ObjectURLConnection.getInputStream();
						FileOutputStream  ObjectFileOutputStream  = new FileOutputStream(ObjectFileDirectory);
						
						Integer ObjectBytesRead = -1;
		                		byte[]  ObjectBuffer    = new byte[4096];
						
						while ((ObjectBytesRead = ObjectInputStream.read()) != -1) {
							
							ObjectFileOutputStream.write(ObjectBuffer, 0, ObjectBytesRead);
							
						}
						
						ObjectFileOutputStream.close();
						
					}
					
				}
				// -- AssetsFolderObjects --
				
				// -- AssetsFolderLogConfigs --
				String LogConfigsRootDirectory = AssetsRootDirectory + "/log_configs/";

				JSONObject MetadataLoggingJSONObject           = (JSONObject) MetadataJSONObject.get("logging");
				JSONObject MetadataLoggingClientJSONObject     = (JSONObject) MetadataLoggingJSONObject.get("client");
				JSONObject MetadataLoggingClientFileJSONObject = (JSONObject) MetadataLoggingClientJSONObject.get("file");
				
				String        LogConfigsURLString     = (String) MetadataLoggingClientFileJSONObject.get("url");
				URL           LogConfigsURL           = new URL(LogConfigsURLString);
				URLConnection LogConfigsURLConnection = LogConfigsURL.openConnection();
				InputStream   LogConfigsInputStream   = LogConfigsURLConnection.getInputStream();
				
				File LogConfigsFile       = new File(LogConfigsRootDirectory + (String) MetadataLoggingClientFileJSONObject.get("id"));
				File LogConfigsDirectory  = new File(LogConfigsRootDirectory);
				
				if (!LogConfigsFile.isFile()) {
					
					if (!LogConfigsDirectory.isDirectory()) {
						
						LogConfigsDirectory.mkdirs();
						
					}
					
					FileOutputStream  LogConfigsFileOutputStream  = new FileOutputStream(LogConfigsFile);
					Integer           LogConfigsBytesRead         = -1;
					byte[]            LogConfigsBuffer            = new byte[4096];

					while ((LogConfigsBytesRead = LogConfigsInputStream.read(LogConfigsBuffer)) != -1) {

						LogConfigsFileOutputStream.write(LogConfigsBuffer, 0, LogConfigsBytesRead);

					}

					LogConfigsFileOutputStream.close();
				}
				// -- AssetsFolderLogConfigs --
				
				// -- AssetsFolder --
				
				// -- LibrariesFolder + BinFolder --
				String BinRootDirectory       = ROOT_DIRECTORY + "/bin/";
				String LibrariesRootDirectory = ROOT_DIRECTORY + "/libraries/";
				
				String  OS         = System.getProperty("os.name").toLowerCase();
				Boolean isMac      = OS.indexOf("mac") >= 0 || OS.indexOf("darwin") >= 0;
				Boolean isWindows  = OS.indexOf("win") >= 0;
				Boolean isLinux    = OS.indexOf("nux") >= 0;
				String  OSPlatform = isMac ? "osx" : (isWindows ? "windows" : (isLinux ? "linux" : null));
				
				JSONArray LibrariesJSONArray = (JSONArray) MetadataJSONObject.get("libraries");
				
				for (Object Library : LibrariesJSONArray) {
					
					JSONObject LibraryJSONObject                     = (JSONObject) Library;
					JSONObject LibraryDownloadsJSONObject            = (JSONObject) LibraryJSONObject.get("downloads");
					JSONObject LibraryDownloadsArtifactJSONObject    = (JSONObject) LibraryDownloadsJSONObject.get("artifact");
					JSONObject LibraryDownloadsClassifiersJSONObject = (JSONObject) LibraryDownloadsJSONObject.get("classifiers");
					JSONArray  LibraryRules                          = (JSONArray) LibraryJSONObject.get("rules");
					JSONObject LibraryNatives                        = (JSONObject) LibraryJSONObject.get("natives");
					
					if (LibraryDownloadsClassifiersJSONObject != null || LibraryNatives != null) {
						
						if (LibraryNatives.containsKey(OSPlatform)) {
							
							String LibraryNativesName = (String) LibraryNatives.get(OSPlatform);
							
							JSONObject LibraryClassifier = (JSONObject) LibraryDownloadsClassifiersJSONObject.get(LibraryNativesName);
							
							String        LibraryClassifierURLString     = (String) LibraryClassifier.get("url");	
							URL           LibraryClassifierURL           = new URL(LibraryClassifierURLString);
							URLConnection LibraryClassifierURLConnection = LibraryClassifierURL.openConnection();
							InputStream   LibraryClassifierInputStream   = LibraryClassifierURLConnection.getInputStream();
							
							String LibraryClassifierPath = (String) LibraryClassifier.get("path");
							
							File LibraryClassifierFile      = new File(LibrariesRootDirectory + LibraryClassifierPath);
							File LibraryClassifierDirectory = new File(LibrariesRootDirectory + LibraryClassifierPath.substring(0, LibraryClassifierPath.lastIndexOf("/")));
							
							if (!LibraryClassifierFile.isFile()) {
								
								if (!LibraryClassifierDirectory.isDirectory()) {
									
									LibraryClassifierDirectory.mkdirs();
								}
								
								FileOutputStream  LibraryClassifierFileOutputStream  = new FileOutputStream(LibraryClassifierFile);
				                		Integer           LibraryClassifierBytesRead         = -1;
				                		byte[]            LibraryClassifierBuffer            = new byte[4096];
				                
								while ((LibraryClassifierBytesRead = LibraryClassifierInputStream.read(LibraryClassifierBuffer)) != -1) {

									LibraryClassifierFileOutputStream.write(LibraryClassifierBuffer, 0, LibraryClassifierBytesRead);

								}
								
				                		LibraryClassifierFileOutputStream.close();
								
							}
							
							String BinNativesRootDirectory = BinRootDirectory + "/natives/";
							
							JarFile               LibraryClassifierJarFile    = new JarFile(LibraryClassifierFile);  
							Enumeration<JarEntry> LibraryClassifierJarEntries = LibraryClassifierJarFile.entries();
							
							while (LibraryClassifierJarEntries.hasMoreElements()) { 
								
							    JarEntry LibraryClassifierJarEntry     = LibraryClassifierJarEntries.nextElement();   
							    String   LibraryClassifierJarEntryName =LibraryClassifierJarEntry.getName();
							    
							    if (LibraryClassifierJarEntryName.endsWith(".dll")) {
							    	
							    	InputStream LibraryClassifierJarInputStream = LibraryClassifierJarFile.getInputStream(LibraryClassifierJarEntry);
							    	
							    	File LibraryNativeFile      = new File(BinNativesRootDirectory + LibraryClassifierJarEntryName);
							    	File LibraryNativeDirectory = new File(BinNativesRootDirectory);     
							    	
							    	if (!LibraryNativeFile.isFile()) {
							    		
							    		if (!LibraryNativeDirectory.isDirectory()) {
							    			
							    			LibraryNativeDirectory.mkdirs();
							    			
							    		}
							    		
							    	}
							    	
							    	FileOutputStream LibraryNativeFileOutputStream = new FileOutputStream(LibraryNativeFile);
							    	
							    	while (LibraryClassifierJarInputStream.available() > 0) {
							    		
							    		LibraryNativeFileOutputStream.write(LibraryClassifierJarInputStream.read());
							            
							        }
						
							    	LibraryNativeFileOutputStream.close();
							    	
							    }
							    
							} 
							
							LibraryClassifierJarFile.close();
						}
						
					}
					
					Boolean isAllowed = false;
					
					if (LibraryRules != null) {
						
						for (Object Rule : LibraryRules) {
							
							JSONObject RuleJSONObject = (JSONObject) Rule;
							JSONObject RuleOS         = (JSONObject) RuleJSONObject.get("os");
							
							String RuleAction = (String) RuleJSONObject.get("action");
								
							if (RuleOS == null && RuleAction.equals("allow")) {
								
								isAllowed = true;
								
							}
							
							else if (RuleOS != null) {
								
								String RuleOSName = (String) RuleOS.get("name");
								
								if (RuleOSName.equals(OSPlatform) && RuleAction.equals("disallow")) {
									
									isAllowed = false;
									
								}
								else if (RuleOSName.equals(OSPlatform) && RuleAction.equals("allow")) {
									
									isAllowed = true;
									
								}
								else {
									
									isAllowed = false;
									
								}
								
							} 
							
							else {
								
								isAllowed = false;
								
							}
							
						}
						
					}
					
					else {
						
						isAllowed = true;
						
					}
					
					if (isAllowed) {
						
						String        LibraryURLString     = (String) LibraryDownloadsArtifactJSONObject.get("url");	
						URL           LibraryURL           = new URL(LibraryURLString);
						URLConnection LibraryURLConnection = LibraryURL.openConnection();
						InputStream   LibraryInputStream   = LibraryURLConnection.getInputStream();
						
						String LibraryPath = (String) LibraryDownloadsArtifactJSONObject.get("path");
						
						File LibraryFile      = new File(LibrariesRootDirectory + LibraryPath);
						File LibraryDirectory = new File(LibrariesRootDirectory + LibraryPath.substring(0, LibraryPath.lastIndexOf("/")));
						
						if (!LibraryFile.isFile()) {
							
							if (!LibraryDirectory.isDirectory()) {
								
								LibraryDirectory.mkdirs();
							}
							
							FileOutputStream  LibraryFileOutputStream  = new FileOutputStream(LibraryFile);
			                Integer           LibraryBytesRead         = -1;
			                byte[]            LibraryBuffer            = new byte[4096];
			                
			                while ((LibraryBytesRead = LibraryInputStream.read(LibraryBuffer)) != -1) {
			                	
			                	LibraryFileOutputStream.write(LibraryBuffer, 0, LibraryBytesRead);
			                	
			                }
							
			                LibraryFileOutputStream.close();
							
						}
						
					}
					
				}
				// -- LibrariesFolder + BinFolder --
				
				// -- VersionsFolder --
				JSONObject MetadataDownloads       = (JSONObject) MetadataJSONObject.get("downloads");
				JSONObject MetadataDownloadsClient = (JSONObject) MetadataDownloads.get("client");
				
				String        ClientURLString     = (String) MetadataDownloadsClient.get("url");
				URL           ClientURL           =  new URL(ClientURLString);
				URLConnection ClientURLConnection = ClientURL.openConnection();
				InputStream   ClientInputStream   = ClientURLConnection.getInputStream();
				
				File ClientFile      = new File(VersionsFileDirectory + "/" + ManifestVersionID + ".jar");
				File ClientDirectory = new File(VersionsFileDirectory);
				
				if (!ClientFile.isFile()) {
					
					if (!ClientDirectory.isDirectory()) {
						
						ClientDirectory.mkdirs();
					}
					
					FileOutputStream  ClientFileOutputStream  = new FileOutputStream(ClientFile);
	                Integer           ClientBytesRead         = -1;
	                byte[]            ClientBuffer            = new byte[4096];
	                
	                while ((ClientBytesRead = ClientInputStream.read(ClientBuffer)) != -1) {
	                	
	                	ClientFileOutputStream.write(ClientBuffer, 0, ClientBytesRead);
	                	
	                }
					
	                ClientFileOutputStream.close();
					
				}
				// -- VersionsFolder --
			}
			
		}
		// -- Metadata --
		
	}
	public static void main(String[] arguments) throws Exception {
		new WML("1.18");
	}
}
