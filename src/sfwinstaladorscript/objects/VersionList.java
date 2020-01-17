package sfwinstaladorscript.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Representa uma lista de versões de um produto.
 */
public class VersionList {

    /**
     * Lista com as versões do produto.
     */
    private ArrayList _versions;

    /**
     * Construtor padrão.
     */
    public VersionList() {
        this._versions = new ArrayList();
    }

    /**
     * Construtor que já recebe a lista de versões.
     * @param versions Lista com as versões do produto.
     */
    public VersionList(ArrayList versions) {
        this._versions = versions;
    }

    /**
     * Adiciona uma versão na lista de versões.
     * @param version Objeto versão.
     */
    public void addVersion(Version version) {
        this._versions.add(version);
    }

    /**
     * Retorna a maior versão existente na lista.
     * @return Objeto versão.
     */
    public Version getMaxVersion() {
        ArrayList v_arraylist_sortedversions;

        if (this._versions.isEmpty()) {
            return null;
        }

        v_arraylist_sortedversions = new ArrayList(this._versions);
        Collections.sort(v_arraylist_sortedversions, Version.get_comparator());
        return (Version) v_arraylist_sortedversions.get(0);
    }

    /**
     * Retorna lista de versões.
     * @return Lista de versões.
     */
    public ArrayList get_versions() {
        return this._versions;
    }

    /**
     * Busca uma versão na lista de acordo com o número passado.
     * @param v Parte versão da versão que se quer achar.
     * @param r Parte release da versão que se quer achar.
     * @param p Parte patch da versão que se quer achar.
     * @return Versão encontrada ou null.
     */
    public Version getVersionByNumber(int v, int r, int p) {
        Iterator v_iterator_it;
        Version v_version_return;

        v_iterator_it = this._versions.iterator();
        while (v_iterator_it.hasNext()) {
            v_version_return = (Version) v_iterator_it.next();

            if (v_version_return.getVersionNumber() == v
                    && v_version_return.getReleaseNumber() == r
                    && v_version_return.getPatchNumber() == p) {
                return v_version_return;
            }
        }

        return null;
    }

    /**
     *
     * @param v
     * @param r
     * @param p
     * @param b
     * @return
     */
    public Version getVersionByNumber(int v, int r, int p, int b) {
        Iterator v_iterator_it;
        Version v_version_return;

        v_iterator_it = this._versions.iterator();
        while (v_iterator_it.hasNext()) {
            v_version_return = (Version) v_iterator_it.next();

            if (v_version_return.getVersionNumber() == v
                    && v_version_return.getReleaseNumber() == r
                    && v_version_return.getPatchNumber() == p
                    && v_version_return.getBuildNumber() == b) {

                return v_version_return;
            }
        }
        return null;
    }

    /**
     * 
     * @param v
     * @param r
     * @param p
     * @param c
     * @return
     */
    public Version getVersionByNumber(int v, int r, int p, int b, int c) {
        Iterator v_iterator_it;
        Version v_version_return;

        v_iterator_it = this._versions.iterator();


        while (v_iterator_it.hasNext()) {
            v_version_return = (Version) v_iterator_it.next();



            if (v_version_return.getVersionNumber() == v
                    && v_version_return.getReleaseNumber() == r
                    && v_version_return.getPatchNumber() == p
                    && v_version_return.getBuildNumber() == b
                    && v_version_return.getCustomNumber() == c) {
                return v_version_return;


            }
        }
        return null;


    }
}
