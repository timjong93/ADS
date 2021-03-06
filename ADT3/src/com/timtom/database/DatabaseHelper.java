package com.timtom.database;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.sound.midi.SysexMessage;

import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;

import com.google.common.primitives.Longs;
import com.timtom.model.Mail;

public class DatabaseHelper {
	private static DatabaseHelper instance;
	private HBaseAdmin admin;
	private Configuration conf;
	private HTable mailTable;
	public DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	public static final String RECIPIENTS = "recipients";
	public static final String SENDER = "sender";
	public static final String CONTENT = "content";
	public static final String META = "meta";
	public static final String ATTACHEMENTS = "attachements";

	private DatabaseHelper() {
		conf = HBaseConfiguration.create();
		try {
			admin = new HBaseAdmin(conf);
			if (!admin.tableExists("Mail")) {
				if (createTable()) {
					System.out.println("Created table \"Mail\"");
				} else {
					System.err.println("failed to create tables");
					System.exit(1);
				}
			}

			mailTable = new HTable(conf, "Mail");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static DatabaseHelper getDatabaseHelper() {
		if (instance == null) {
			instance = new DatabaseHelper();
		}
		return instance;
	}

	public boolean createTable() {
		try {
			HTableDescriptor tableDescriptor = new HTableDescriptor("Mail");
			tableDescriptor.addFamily(new HColumnDescriptor(RECIPIENTS));
			tableDescriptor.addFamily(new HColumnDescriptor(SENDER));
			tableDescriptor.addFamily(new HColumnDescriptor(CONTENT));
			tableDescriptor.addFamily(new HColumnDescriptor(META));
			tableDescriptor.addFamily(new HColumnDescriptor(ATTACHEMENTS));
			admin.createTable(tableDescriptor);
			return true;
		} catch (MasterNotRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public void closeConnection() {
		try {
			mailTable.close();
			admin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertMail(Mail mail) throws NoSuchAlgorithmException,
			IOException {
		ArrayList<Put> puts = new ArrayList<Put>();
		// For each type of receiver (normal/cc or bcc) do: 
		for (int i = 0; i < mail.recievers.size() + mail.ccRecievers.size()
				+ mail.bccRecievers.size(); i++) {
			Put put = null;
			// if we are still handling normal receivers do to get the receiver out of the correct array:
			if (i < mail.recievers.size()) {
				put = new Put(mail.getKey(mail.recievers.get(i)));
			} 
			// if we are handling cc receivers do this to get the receiver out of the correct array:
			else if (i < mail.recievers.size() + mail.ccRecievers.size()) {
				put = new Put(mail.getKey(mail.ccRecievers.get(i
						- mail.recievers.size())));
			} 
			// finally if we are handling bcc receivers do this to get the receiver out of the correct array:
			else {
				put = new Put(mail.getKey(mail.bccRecievers.get(i
						- (mail.recievers.size() + mail.ccRecievers.size()))));
				put.add(Bytes.toBytes(RECIPIENTS),
						Bytes.toBytes(mail.bccRecievers.get(i
								- (mail.recievers.size() + mail.ccRecievers
										.size()))), Bytes.toBytes("BCC"));
			}

			for (String s : mail.recievers) {
				put.add(Bytes.toBytes(RECIPIENTS), Bytes.toBytes(s),
						Bytes.toBytes("REC"));
			}
			for (String s : mail.ccRecievers) {
				put.add(Bytes.toBytes(RECIPIENTS), Bytes.toBytes(s),
						Bytes.toBytes("CC"));
			}

			put.add(Bytes.toBytes(SENDER), Bytes.toBytes("name"),
					Bytes.toBytes(mail.sender));

			put.add(Bytes.toBytes(CONTENT), Bytes.toBytes("subject"),
					Bytes.toBytes(mail.subject));
			put.add(Bytes.toBytes(CONTENT), Bytes.toBytes("body"),
					Bytes.toBytes(mail.mailBody));

			put.add(Bytes.toBytes(META), Bytes.toBytes("send_time"),
					Longs.toByteArray(mail.sendTime.getTime()));
			put.add(Bytes.toBytes(META), Bytes.toBytes("read"),
					Bytes.toBytes(false));

			for (File f : mail.attachements) {
				put.add(Bytes.toBytes(ATTACHEMENTS),
						Bytes.toBytes(f.getName()),
						Bytes.toBytes(f.getAbsolutePath()));
			}

			puts.add(put);
		}

		mailTable.put(puts);
		mailTable.flushCommits();
	}

	public Mail getMail(String owner, Mail m) {
		try {
			Get get = new Get(m.getKey(owner));
			get.addFamily(CONTENT.getBytes());
			get.addFamily(SENDER.getBytes());
			get.addFamily(RECIPIENTS.getBytes());
			get.addFamily(META.getBytes());
			Result result = mailTable.get(get);

			return Mail.parseResult(result);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<Mail> getInbox(String owner) throws IOException,
			NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(owner.getBytes());
		byte[] ownerHashed = md.digest();
		md.reset();
		byte[] prefix = Arrays.copyOfRange(ownerHashed, 0,
				ownerHashed.length < 20 ? ownerHashed.length : 20);

		Scan scan = new Scan();
		Filter prefixFilter = new PrefixFilter(prefix);
		scan.setFilter(prefixFilter);
		scan.addColumn(Bytes.toBytes("sender"), Bytes.toBytes("name"));
		scan.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("send_time"));
		scan.addColumn(Bytes.toBytes("content"), Bytes.toBytes("subject"));
		scan.addColumn(Bytes.toBytes("content"), Bytes.toBytes("body"));
		scan.addFamily(META.getBytes());

		ResultScanner scanner = mailTable.getScanner(scan);
		ArrayList<Mail> inbox = new ArrayList<Mail>();
		for (Result result : scanner) {
			inbox.add(Mail.parseResult(result));
		}
		return inbox;
	}

	public void removeMail(String owner, Mail mail) {
		try {
			Delete delete = new Delete(mail.getKey(owner));

			mailTable.delete(delete);

			mailTable.flushCommits();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Mail> searchMail(String owner, String word)
			throws NoSuchAlgorithmException, IOException {
		List<Mail> results = new ArrayList<Mail>();

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(owner.getBytes());
		byte[] ownerHashed = md.digest();
		md.reset();
		byte[] prefix = Arrays.copyOfRange(ownerHashed, 0,
				ownerHashed.length < 20 ? ownerHashed.length : 20);

		Scan scan = new Scan(prefix);
		scan.addFamily(CONTENT.getBytes());
		scan.addFamily(SENDER.getBytes());
		scan.addFamily(RECIPIENTS.getBytes());
		scan.addFamily(META.getBytes());

		// FilterChain chain = new Filter
		Filter filter1 = new SingleColumnValueFilter(CONTENT.getBytes(),
				Bytes.toBytes("subject"), CompareOp.EQUAL,
				new SubstringComparator(word));
		Filter filter2 = new SingleColumnValueFilter(CONTENT.getBytes(),
				Bytes.toBytes("body"), CompareOp.EQUAL,
				new SubstringComparator(word));
		FilterList fl = new FilterList(Operator.MUST_PASS_ONE, filter1, filter2);
		scan.setFilter(fl);

		ResultScanner rs = mailTable.getScanner(scan);

		for (Result result : rs) {
			results.add(Mail.parseResult(result));
		}

		return results;
	}

	public List<String> searchContacts(String owner) throws IOException,
			NoSuchAlgorithmException {
		Set<String> results = new HashSet<String>();

		Scan scan = new Scan();
		scan.addFamily(RECIPIENTS.getBytes());
		Filter filter1 = new SingleColumnValueFilter(SENDER.getBytes(),
				Bytes.toBytes("name"), CompareOp.EQUAL, owner.getBytes());
		scan.setFilter(filter1);

		scan.addFamily(RECIPIENTS.getBytes());
		scan.addFamily(SENDER.getBytes());

		ResultScanner rs = mailTable.getScanner(scan);

		for (Result result : rs) {
			Mail m = Mail.parseResult(result);
			results.addAll(m.recievers);
			results.addAll(m.ccRecievers);
			results.addAll(m.bccRecievers);
		}

		ArrayList<Mail> mails = getInbox(owner);

		for (Mail m : mails) {
			results.add(m.sender);
		}

		return new ArrayList<String>(results);
	}

	public void setLabel(String owner, Mail mail, String label)
			throws IOException, NoSuchAlgorithmException {
		Put put = new Put(mail.getKey(owner));
		put.add(Bytes.toBytes("meta"), Bytes.toBytes("label"),
				Bytes.toBytes(label));
		mailTable.put(put);
		mailTable.flushCommits();
	}

	public int nrOfReceivedMails(String owner) throws NoSuchAlgorithmException,
			IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(owner.getBytes());
		byte[] ownerHashed = md.digest();
		md.reset();
		byte[] prefix = Arrays.copyOfRange(ownerHashed, 0,
				ownerHashed.length < 20 ? ownerHashed.length : 20);
		Scan scan = new Scan();
		Filter prefixFilter = new PrefixFilter(prefix);
		scan.setFilter(prefixFilter);
		ResultScanner scanner = mailTable.getScanner(scan);
		int result = 0;
		for (Result r : scanner) {
			result++;
		}
		return result;
	}

	public int nrOfSendMails(String owner) throws NoSuchAlgorithmException,
			IOException {
		Scan scan = new Scan();
		Filter filter1 = new SingleColumnValueFilter(SENDER.getBytes(),
				Bytes.toBytes("name"), CompareOp.EQUAL, owner.getBytes());
		scan.setFilter(filter1);
		
		ResultScanner scanner = mailTable.getScanner(scan);
		int result = 0;
		for (Result r : scanner) {
			result++;
		}
		return result;
	}

}
