/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfwinstaladorscript.comparabase.objects;

import Interfaces.Objects;

/**
 *
 * @author jopaulo
 */
public class Package_Body implements Objects {

    private String NAME;
    private int LINE_COUNT;

    public int getLINE_COUNT() {
        return LINE_COUNT;
    }

    public void setLINE_COUNT(int LINE_COUNT) {
        this.LINE_COUNT = LINE_COUNT;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Package_Body) {
            Package_Body proc = (Package_Body) obj;
            return proc.getNAME().equals(this.getNAME());
        } else {
            return false;
        }
    }

    public boolean equalsLineCount(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Package_Body) {
            Package_Body proc = (Package_Body) obj;
            return proc.getLINE_COUNT() == this.getLINE_COUNT();
        } else {
            return false;
        }
    }
}
