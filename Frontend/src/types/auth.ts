export interface AuthUser {
  email: string;
  role: string;
}

export interface AuthContextType {
  user: AuthUser | null;
  login: (token: string) => void;
  logout: () => void;
  isAuthenticated: boolean;
}
