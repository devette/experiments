package nl.demo;

/**
 *  Generated: ${.now}
 */
public enum ${clazz.label} {

<#if (clazz.individualsCount > 0) >
	<#list clazz.individuals as country>
		${country.label?replace(".","_")} (<#rt>
		     "${country.label}",<#rt>
             "${country.getDataProperty("countryCodeUNNumeric3").object.literal!}",<#rt>
             "${country.getDataProperty("countryNameISO3166Short").object.literal!}",<#rt>
             "${country.getDataProperty("countryCodeISO3166Alpha2").object.literal!}",<#rt>
             "${country.getDataProperty("countryCodeISO3166Alpha3").object.literal!}",<#rt>
             <#list country.getObjectProperties("referencesCountry") as referencesCountry>
                 <#if referencesCountry.object.getDataProperty("nameLocalLong")??>
                     "${referencesCountry.object.getDataProperty("nameLocalLong").object.literal!}"<#rt>
                 <#else>
                     "${country.label}"<#rt>
                 </#if>
             </#list>
        )<#sep>,</#sep><#lt>
	</#list>;
</#if>


   private final String id;
   private final String countryCodeUNNumeric3;
   private final String countryNameISO3166Short;
   private final String countryCodeISO3166Alpha2;
   private final String countryCodeISO3166Alpha3;
   private final String longName;

   ISO3166DefinedCountry(final String id,
          final String countryCodeUNNumeric3,
          final String countryNameISO3166Short,
          final String countryCodeISO3166Alpha2,
          final String countryCodeISO3166Alpha3,
          final String longName)  {
          this.id = id;
          this.countryCodeUNNumeric3 = countryCodeUNNumeric3;
          this.countryNameISO3166Short = countryNameISO3166Short;
          this.countryCodeISO3166Alpha2 = countryCodeISO3166Alpha2;
          this.countryCodeISO3166Alpha3 = countryCodeISO3166Alpha3;
          this.longName = longName;
   }

   public String getId() {
       return id;
   }

   public String getCountryCodeUNNumeric3() {
       return countryCodeUNNumeric3;
   }

   public String getCountryNameISO3166Short() {
       return countryNameISO3166Short;
   }

   public String getCountryCodeISO3166Alpha2() {
       return countryCodeISO3166Alpha2;
   }

   public String getCountryCodeISO3166Alpha3() {
       return countryCodeISO3166Alpha3;
   }

   public String getLongName() {
	   return longName;
   }
}