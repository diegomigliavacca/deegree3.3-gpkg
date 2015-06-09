This is my new fork of the deegree3 repository (https://github.com/deegree/deegree3), branch 3.3-master, version 3.3.15. It includes an OGC GeoPackage implementation and a fix for the WMTS service.

**INFOS**

"The GeoPackage specification describes an open, standards-based, platform-independent, portable, self-describing, compact format for transferring geospatial information. It is a set of conventions for SQLite to store interoperable Features and/or Tiles on a common base" (opengis/geopackage GitHub repository).

In order to access a Geopackage file, you need to create a JDBC connection which points to the Geopackage file.

```
<JDBCConnection xmlns="http://www.deegree.org/jdbc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" configVersion="3.0.0" xsi:schemaLocation="http://www.deegree.org/jdbc http://schemas.deegree.org/jdbc/3.0.0/jdbc.xsd">
  <Url>jdbc:sqlite:/home/user/World.gpkg</Url>
  <User>null</User>
  <Password>null</Password>
</JDBCConnection>
```

User and Password can just to be set to "null", given that GeoPackage is based on SQLite.

For the feature part, the user needs to configure in Deegree:
an SQL feature store;
a WFS service;
a Feature Layer.

For the tile part, the user needs to configure:
a WMTS service;
a Gpkg Tile Store;
a Tile Layer.

Configuration of a Gpkg Tile Store (example):

```
<GpkgTileStore xmlns="http://www.deegree.org/datasource/tile/gpkg" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.deegree.org/datasource/tile/gpkg http://schemas.deegree.org/datasource/tile/gpkg/3.2.0/geopackage.xsd" configVersion="3.2.0">

  <TileDataSet>
    <JDBCConnId>gpkg</JDBCConnId>
    <Identifier>gpkg_tilelayer</Identifier>
    <TileMapping table="fromosm_tiles"/>
    <!-- [0..1]: the mime type of the desired image output format. Default is image/png -->
    <ImageFormat>image/png</ImageFormat>
</TileDataSet>

</GpkgTileStore>
```
