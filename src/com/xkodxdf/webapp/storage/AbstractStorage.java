package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.exception.ExistStorageException;
import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());


    @Override
    public final void save(Resume r) {
        LOG.info("Save: " + r);
        SK searchKey = getNotExistingSearchKey(r.getUuid());
        doSave(r, searchKey);
    }

    @Override
    public final Resume get(String uuid) {
        LOG.info("Get: " + uuid);
        SK searchKey = getExistingSearchKey(uuid);
        return doGet(searchKey);
    }

    @Override
    public void update(Resume r) {
        LOG.info("Update: " + r);
        SK searchKey = getExistingSearchKey(r.getUuid());
        doUpdate(r, searchKey);
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete: " + uuid);
        SK searchKey = getExistingSearchKey(uuid);
        doDelete(searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        Comparator<Resume> resumeComparator = Comparator.comparing(Resume::getFullName)
                .thenComparing(Resume::getUuid);
        List<Resume> ret = doCopy();
        ret.sort(resumeComparator);
        return ret;
    }


    private SK getExistingSearchKey(String uuid) {
        LOG.info("getExistingSearchKey: " + uuid);
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            LOG.warning("Resume with uuid: " + uuid + " not exist.");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK getNotExistingSearchKey(String uuid) {
        LOG.info("getNotExistingSearchKey: " + uuid);
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            LOG.warning("Resume with uuid: " + uuid + " already exist.");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }


    protected abstract boolean isExist(SK searchKey);

    protected abstract SK getSearchKey(String uuid);

    protected abstract void doSave(Resume r, SK searchKey);

    protected abstract Resume doGet(SK searchKey);

    protected abstract void doUpdate(Resume r, SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract List<Resume> doCopy();
}