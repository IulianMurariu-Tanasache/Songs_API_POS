//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.12.14 at 04:06:17 PM EET 
//


package com.pos.gateway.soap;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="jwt" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "jwt"
})
@XmlRootElement(name = "authRequest")
@JacksonXmlRootElement(namespace = "idm_namespace", localName = "authRequest")
public class AuthRequest {

    @JacksonXmlProperty(namespace = "idm_namespace")
    @XmlElement(required = true)
    protected String jwt;

    /**
     * Gets the value of the jwt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJwt() {
        return jwt;
    }

    /**
     * Sets the value of the jwt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJwt(String value) {
        this.jwt = value;
    }

}