from ldap3 import Server, Connection, ALL, SUBTREE
from ldap3.core.exceptions import LDAPException
import csv

def get_ldap_users():
	server = Server('10.124.155.18', port=1389, get_info=ALL)
	conn = Connection(server, 'cn=oudadmin', 'Oracle123', auto_bind=True)
	try:
		conn.search(search_base='cn=Users,dc=sassa,dc=gov,dc=za',
					search_filter='(objectclass=person)',
					attributes=['givenName', 'sn', 'uid', 'mail','employeeNumber','displayName'])
		results = conn.entries
	except LDAPException as e:
		results = e
	return results

if __name__ == "__main__":
	entries = get_ldap_users()
	if entries is None or len(entries) == 0:
		print('no records found')
	else:
		with open('oud_dev.csv', mode='w') as csv_file:
			writer = csv.writer(csv_file, delimiter=',')
			writer.writerow(['nr', 'givenName', 'sn', 'uid', 'mail','employeeNumber','displayName'])
			for idx, entry in enumerate(entries):
				writer.writerow([idx+1, entry['givenName'], entry['sn'], entry['uid'], entry['mail'], entry['employeeNumber'], entry['displayName']])
