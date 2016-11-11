<#setting url_escaping_charset="UTF-8">
<!doctype html>
<!-- Copyright (c) 2014 Google Inc. All rights reserved. -->
<html>
<head>
  <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes">
  <title>Google Map demo</title>
  <script src="bower_components/webcomponentsjs/webcomponents.min.js"></script>
  <link rel="import" href="bower_components/google-map/google-map.html">
  <link rel="import" href="bower_components/google-map/google-map-directions.html">
  <style>
    #controlsToggle {
      position: absolute;
      left: 10%;
      bottom: 10%;
    }
  </style>
</head>
<body fullbleed unresolved>
<h1>${individual.getLocalLabel(context.locale)}</h1>


		<br/>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">Eigenschappen</h4>
			</div>
			<div id="dataPropertiesPanel" class="panel-collapse expand">
				<div class="panel-body">
					<table style="width:100%">
						<#list individual.dataProperties as p>
							<tr>
			                    <td><a href="select?dataproperty=${p.iri?url}"><nobr><i class="fa fa-circle"></i> ${p.getLocalLabel(context.locale)}</nobr></a></td>               
			                    <td style="vertical-align:top;">${p.object.literal!}</td>
			                    <!--<td>${p.object.lang!}</td>-->
			                </tr>
						</#list>
					</table>
				</div>
			</div>
		</div>

		<br/>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">Relaties (Uit)</h4>
			</div>
			<div id="objectPropertiesPanel" class="panel-collapse expand">
				<div class="panel-body">
					<table style="width:100%">
						<tr><th style="width:20%"></th><th></th><th></th><th></th></tr>
						<#list individual.objectProperties as p>
							<tr>
								<td><a href="select?individual=${p.subject.iri?url}"><nobr><i class="fa fa-circle-o"></i> ${p.subject.getLocalLabel(context.locale)}</nobr></a></td>										
			                    <td><a href="select?objectproperty=${p.iri?url}"><nobr><i class="fa fa-long-arrow-right"></i> ${p.getLocalLabel(context.locale)}</nobr></a></td>
			                    <td><a href="select?individual=${p.object.iri?url}"><nobr><i class="fa fa-circle-o"></i> ${p.object.getLocalLabel(context.locale)}</nobr></a></td>
			                    <td><#list p.object.classes as clazz><a href="select?class=${clazz.iri?url}"><nobr><i class="fa fa-bullseye"></i> ${clazz.getLocalLabel(context.locale)}</nobr></a> </#list></td>
			                </tr>
						</#list>
					</table>
				</div>
			</div>
		</div>
	
	
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					Opmerkingen
				</h4>
			</div>
			<div id="annotationsPanel" class="panel-collapse expand">
				<div class="panel-body">
					<table>
						<#list individual.annotations as annotation>
							<tr>
							  <td style="vertical-align:top;">${annotation.property!}</td>
		                      <td>${annotation.value.literal!}</td>
		                      <!--<td>${annotation.value.lang!}</td>-->
		                    </tr>
						</#list>
					</table>
				</div>
			</div>
		</div>

<#assign lat = "37.779">
<#assign long = "-122.3892">
<#list individual.getDataProperties("lat") as latitude>
	<#assign lat = latitude.object.literal!>
	Latitude: ${latitude.object.literal!}
	Lat: ${lat!}
</#list>
<br/>
<#list individual.getDataProperties("long") as longitude>
	<#assign long = longitude.object.literal!>
	Longitude: ${longitude.object.literal!}
	Long: ${long!}
</#list>

<google-map style="width:200px; height:200px" latitude="${lat}" longitude="${long}" minZoom="9" maxZoom="11">
  <google-map-marker latitude="${lat}" longitude="${long}"
                     title="Go Giants!" draggable="true">
    <img src="http://upload.wikimedia.org/wikipedia/commons/thumb/4/49/San_Francisco_Giants_Cap_Insignia.svg/200px-San_Francisco_Giants_Cap_Insignia.svg.png" />
  </google-map-marker>
</google-map>

<button id="controlsToggle" onclick="toggleControls()">Toggle controls</button>

<script>
  var gmap = document.querySelector('google-map');
  gmap.addEventListener('api-load', function(e) {
    document.querySelector('google-map-directions').map = this.map;
  });

  function toggleControls() {
    gmap.disableDefaultUI = !gmap.disableDefaultUI;
  }
</script>
</body>
</html>